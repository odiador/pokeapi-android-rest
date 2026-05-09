GRADLEW ?= ./gradlew
GRADLE_ARGS ?=
ADB ?= adb
PACKAGE_NAME ?= co.edu.uniquindio.ingesis.pokeapi

.PHONY: help clean build assembleDebug build-release bundle install-debug uninstall run devices logcat test androidTest detekt ktlint spotless format check check-all doctor dependencies tasks

help:
	@echo "Comandos disponibles:"
	@echo ""
	@echo "  make build-debug      -> Compila APK debug"
	@echo "  make build-release    -> Compila APK release"
	@echo "  make install-debug    -> Instala APK debug"
	@echo "  make uninstall        -> Desinstala la app ($(PACKAGE_NAME))"
	@echo "  make run              -> Instala y abre la app en el dispositivo"
	@echo "  make clean            -> Limpia los artefactos de build"
	@echo "  make test             -> Ejecuta unit tests"
	@echo "  make androidTest      -> Ejecuta instrumented tests (requiere dispositivo)"
	@echo "  make lint             -> Ejecuta Android lint"
	@echo "  make detekt           -> Análisis estático con Detekt"
	@echo "  make format           -> Aplica formato automático (Spotless/ktlint)"
	@echo "  make check            -> Ejecuta todas las validaciones (lint, test, detekt, spotless)"
	@echo "  make devices          -> Lista dispositivos ADB"
	@echo "  make logcat           -> Muestra logs del dispositivo"
	@echo "  make doctor           -> Verifica versiones de herramientas"
	@echo ""

clean:
	$(GRADLEW) $(GRADLE_ARGS) clean

build-debug:
	$(GRADLEW) $(GRADLE_ARGS) assembleDebug

build-release:
	$(GRADLEW) $(GRADLE_ARGS) assembleRelease

bundle:
	$(GRADLEW) $(GRADLE_ARGS) bundleRelease

assembleDebug: build-debug

install-debug:
	$(GRADLEW) $(GRADLE_ARGS) installDebug

uninstall:
	$(ADB) uninstall $(PACKAGE_NAME) || true

run: install-debug
	$(ADB) shell monkey -p $(PACKAGE_NAME) -c android.intent.category.LAUNCHER 1

devices:
	$(ADB) devices

logcat:
	$(ADB) logcat

lint:
	$(GRADLEW) $(GRADLE_ARGS) lint

test:
	$(GRADLEW) $(GRADLE_ARGS) test

androidTest:
	$(GRADLEW) $(GRADLE_ARGS) connectedAndroidTest

detekt:
	$(GRADLEW) $(GRADLE_ARGS) detekt

ktlint:
	$(GRADLEW) $(GRADLE_ARGS) ktlintCheck

spotless:
	$(GRADLEW) $(GRADLE_ARGS) spotlessCheck

format:
	$(GRADLEW) $(GRADLE_ARGS) spotlessApply

check:
	$(GRADLEW) $(GRADLE_ARGS) lint test detekt spotlessCheck

check-all:
	$(GRADLEW) $(GRADLE_ARGS) lint test detekt spotlessCheck connectedAndroidTest

dependencies:
	$(GRADLEW) $(GRADLE_ARGS) :app:dependencies

tasks:
	$(GRADLEW) $(GRADLE_ARGS) tasks

doctor:
	java -version
	$(GRADLEW) --version
	$(ADB) version
