FROM mcr.microsoft.com/devcontainers/jekyll:bullseye

RUN apt-get update && export DEBIAN_FRONTEND=noninteractive \
    && apt-get install -y \
    build-essential python3-dev libcairo2-dev libpango1.0-dev ffmpeg python3-pip \
    texlive texlive-latex-extra dvisvgm \
    freeglut3-dev
