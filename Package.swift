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
            url: "https://github.com/dtsiflit/eudi-lib-kmp-etsi-1196x2/releases/download/0.0.4/EudiEtsi1196x2.xcframework.zip",
            checksum: "89f0f873a466d0b1c85af49adbaeb13f8ade7fb0d8416536951904a485af8dd5"
        )
    ]
)
