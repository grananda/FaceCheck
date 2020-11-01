config ?= compileClasspath

clean:
	./gradlew clean
	rm -rf .db

build:
	./gradlew assemble
	./gradlew facecheck-api:jibDockerBuild
	#docker build -t facecheck-spa:latest facecheck-spa/

run:
	docker-compose up

dev:
	docker-compose -f docker-compose-dev.yml up
