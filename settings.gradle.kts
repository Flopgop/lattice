rootProject.name = "lattice"

include("demo")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("libs.versions.toml"))
        }
        create("tests") {
            from(files("tests.versions.toml"))
        }
    }
}