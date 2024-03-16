setup:
	./gradlew wrapper --gradle-version 8.4

clean:
	./gradlew clean

build:
	./gradlew clean build --stacktrace

install:
	./gradlew installDist

run-dist:
	./build/install/app/bin/app

run:
	./gradlew run

test:
	./gradlew test jacocoTestReport

lint:
	./gradlew checkstyleMain

check-deps:
	./gradlew dependencyUpdates -Drevision=release

report:
	./gradlew jacocoTestReport
