#!/bin/sh

SCENES=$(cat src/scenes.txt)
echo "src/main.py ${SCENES}" | xargs manim
echo "convert ${SCENES} _site/index.html" | xargs manim-slides

if [ ! -z $1 ] && [ $1 = "final" ]; then
    echo "convert --to=pdf ${SCENES} _site/centerstage-presentation.pdf" | xargs manim-slides
fi
