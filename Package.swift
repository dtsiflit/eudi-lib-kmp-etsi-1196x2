// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "EudiEtsi1196x2",
    platforms: [
        .iOS(.v14)
    ],
    products: [
        .library(
            name: "EudiEtsi1196x2",
            targets: ["EudiEtsi1196x2"]
        )
    ],
    targets: [
        .binaryTarget(
            name: "EudiEtsi1196x2",
            url: "https://github.com/dtsiflit/eudi-lib-kmp-etsi-1196x2/releases/download/0.0.2/EudiEtsi1196x2.xcframework.zip",
            checksum: "bb1953fcac1f88ba9fb3b6f1d8167c7adadc346b2df5a034fd8a3c5cd6ffc304"
        )
    ]
)
