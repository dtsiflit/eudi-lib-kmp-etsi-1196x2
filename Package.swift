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
            url: "https://github.com/dtsiflit/eudi-lib-kmp-etsi-1196x2/releases/download/0.1.1/EudiEtsi1196x2.xcframework.zip",
            checksum: "1f9641957e1dab2aef0e5921c29035928bc3c741b10d5aa39e91f097b7f8e1a0"
        )
    ]
)
