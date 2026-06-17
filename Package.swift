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
            url: "https://github.com/dtsiflit/eudi-lib-kmp-etsi-1196x2/releases/download/0.0.3/EudiEtsi1196x2.xcframework.zip",
            checksum: "d0a9a62716c7befd978e2373ef1707f49663c94dd413d56027535d8a52139fa9"
        )
    ]
)
