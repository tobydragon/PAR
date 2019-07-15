class PageImage{
    constructor(imageUrl){
        //Check to see if the canvas has a context
        if (!canvasSupport()) {
            return; //Canvas not supported so exit the function
        }

        var image;
        var imageSource;

        function loadImages(images, imageSources, callback) {
            imageSource = imageUrl;

            image = new Image();
            image.onload = function () {
                callback()
            };
            image.src = imageSource;

        }

        //Setup the canvas object
        var theCanvas = document.getElementById("myCanvas"); //get the canvas element
        var context = theCanvas.getContext("2d"); //get the context
        var canvasHeight = theCanvas.height; //get the heigth of the canvas
        var canvasWidth = theCanvas.width; //get the width of the canvas
        var canvasColor = "white"; // set the default canvas bg color

        function clearCanvas(canvasColor) {
            context.fillStyle = canvasColor;
            context.fillRect(0, 0, canvasWidth, canvasHeight);
        }

        function drawCanvas() {
            clearCanvas(canvasColor);
            context.drawImage(image, 0, 0, canvasWidth, canvasHeight);
        }

        loadImages(image, imageSource, function () {
            drawCanvas()
        });

    }
}

function canvasSupport() {
    return Modernizr.canvas;
}


