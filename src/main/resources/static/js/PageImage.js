class PageImage {

    constructor(imageUrl, canvasName) {
        //Check to see if the canvas has a context
        if (!canvasSupport()) {
            return; //Canvas not supported so exit the function
        }
        this.imageUrl = imageUrl;
        this.canvasName = canvasName;
        this.element = createCanvas(this.imageUrl, this.canvasName);

    }
    loadImage() {
        loadImageOnCanvas(this.imageUrl, this.element);
    }
}

function loadImageOnCanvas(imageUrl, element) {

    //Setup the canvas object
    let context = element.getContext("2d"); //get the context
    let canvasWidth = element.width; //get the width of the canvas
    let canvasHeight = element.height; //get the heigth of the canvas
    let canvasColor = "white"; // set the default canvas bg color

    function clearCanvas(canvasColor) {
        context.fillStyle = canvasColor;
        context.fillRect(0, 0, canvasWidth, canvasHeight);
    }

    function drawCanvas() {
        clearCanvas(canvasColor);
        context.drawImage(image, 0, 0, canvasWidth, canvasHeight);
    }

    var image;
    var imageSource;

    function loadImages(callback) {
        imageSource = imageUrl;
        image = new Image();
        image.src = imageSource;
        image.onload = function () {
            callback()
        };

    }

    loadImages(function () {
        drawCanvas()
    });
}

function createCanvas(imageUrl, name) {
    let newCanvas = document.createElement("CANVAS");
    newCanvas.setAttribute('id', name);
    newCanvas.width = "1024";
    newCanvas.height = "768";
    newCanvas.classList.add("canvas");
    return newCanvas;
}

function canvasSupport() {
    return Modernizr.canvas;
}
