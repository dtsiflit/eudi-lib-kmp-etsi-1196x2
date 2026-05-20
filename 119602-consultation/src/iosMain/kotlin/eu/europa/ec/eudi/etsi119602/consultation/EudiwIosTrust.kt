/*
 * Copyright (c) 2026 European Commission
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.europa.ec.eudi.etsi119602.consultation

import eu.europa.ec.eudi.etsi119602.Uri
import eu.europa.ec.eudi.etsi1196x2.consultation.ComposeChainTrust
import eu.europa.ec.eudi.etsi1196x2.consultation.SupportedLists
import eu.europa.ec.eudi.etsi1196x2.consultation.VerificationContext
import platform.Foundation.NSData

/**
 * One-call assembly of the iOS LoTE-based trust validator, intended for Swift consumers.
 *
 * It wires together: a Darwin [io.ktor.client.HttpClient] → [DownloadSingleLoTE] →
 * [LoadLoTEAndPointers] → [ProvisionTrustAnchorsFromLoTEs.eudiwIos] → [ComposeChainTrust.nonCached],
 * and exposes Swift-friendly entry points that avoid Kotlin value classes ([Uri], [NonEmptyList])
 * at the boundary — LoTE locations are passed as plain URL strings and trust anchors come back as
 * a plain list of DER [NSData].
 */
public object EudiwIosTrust {

    /**
     * Builds a non-cached validator for the given per-context LoTE download URLs. Pass `null` for
     * contexts you do not need. Suitable for low-concurrency use (e.g. one validation per screen).
     *
     * @param verifyJwtSignature verifies each downloaded LoTE JWT — supply a real implementation in
     *        production; this is a required, explicit choice so trust is never silently bypassed.
     */
    public fun nonCached(
        pidProvidersUrl: String?,
        walletProvidersUrl: String?,
        wrpacProvidersUrl: String?,
        wrprcProvidersUrl: String?,
        pubEaaProvidersUrl: String?,
        qeaProvidersUrl: String?,
        verifyJwtSignature: VerifyJwtSignature,
    ): ComposeChainTrust<List<NSData>, VerificationContext, NSData> {
        val locations = SupportedLists(
            pidProviders = pidProvidersUrl?.let(::Uri),
            walletProviders = walletProvidersUrl?.let(::Uri),
            wrpacProviders = wrpacProvidersUrl?.let(::Uri),
            wrprcProviders = wrprcProvidersUrl?.let(::Uri),
            pubEaaProviders = pubEaaProvidersUrl?.let(::Uri),
            qeaProviders = qeaProvidersUrl?.let(::Uri),
        )
        val httpClient = IosLoTEHttpClient.create()
        val downloadSingleLoTE = DownloadSingleLoTE(httpClient)
        val loadLoTEAndPointers = LoadLoTEAndPointers(
            constraints = LoadLoTEAndPointers.Constraints.DoNotLoadOtherPointers,
            verifyJwtSignature = verifyJwtSignature,
            loadLoTE = downloadSingleLoTE,
        )
        return ProvisionTrustAnchorsFromLoTEs
            .eudiwIos(loadLoTEAndPointers = loadLoTEAndPointers)
            .nonCached(locations)
    }

    /**
     * Resolves the trust anchors (DER bytes) for [context] using [validator], as a plain list.
     * Returns an empty list if the validator has no anchors for that context. Suspends while the
     * underlying LoTE is fetched/parsed.
     *
     * `@Throws` is required: without it, any failure (network, JSON parse, cancellation) would be
     * an unhandled Kotlin/Native exception that crashes the process instead of surfacing to Swift
     * as a catchable error.
     */
    @Throws(Throwable::class)
    public suspend fun trustAnchors(
        validator: ComposeChainTrust<List<NSData>, VerificationContext, NSData>,
        context: VerificationContext,
    ): List<NSData> =
        validator.getTrustAnchors.invoke(context)?.list ?: emptyList()
}
