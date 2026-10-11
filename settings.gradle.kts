pluginManagement {
    // Подключаем билд-логику ПЕРВОЙ, чтобы плагины были доступны ниже
    includeBuild("build-logic")

    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    // Запрещаем модулям объявлять свои репозитории для стабильности сборки
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "IP Calculator"

// Feature модули
include(":app")
include(":feature:info")
include(":feature:settings")
include(":feature:calculator")

// Core модули
include(":core:model")
include(":core:ui")
include(":core:datastore")
include(":core:data")
