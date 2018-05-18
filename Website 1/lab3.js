
//menu in mobile version

  function myFunction() {
    var x = document.getElementById("myTopnav");
    if (x.className === "topnav") {
        x.className += " responsive";
    } else {
        x.className = "topnav";
    }
}

// for images (play)



 

// css file options
window.onload = setStyleSheet;

function onChange(value) {

    var theSelect = choose;

    switch (theSelect.selectedIndex) {

        case 1:
            swapStyleSheet('lab3.css');
            break;
        case 2:
            swapStyleSheet('pink.css');
            break;
        case 3:
            swapStyleSheet('green.css');
            break;

    }
}

function swapStyleSheet(sheet) {

    localStorage.style = sheet;
    document.getElementById('styleSheet').href = sheet;

}

 //local storage
function setStyleSheet() {

    if (localStorage.style) {
        document.getElementById('styleSheet').href = localStorage.style;
    } else {
        document.getElementById('styleSheet').href = 'lab3.css';
    }
     var value = localStorage.style;
    if (value == "lab3.css") {
        document.getElementById('choose').value = 1;
    } else if (value == "pink.css") {
        document.getElementById('choose').value = 2;
    } else if (value == "green.css") {
        document.getElementById('choose').value = 3;
    }
    
    var slideIndex = 0;
showSlides();

function showSlides() {
	var i;
	var slides = document.getElementsByClassName("slides");
	var indexTexts = document.getElementsByClassName("slide-index");
	for(i = 0; i < slides.length; i++) {
		slides[i].style.display = "none";
	}
	
	slides[slideIndex].style.display = "block";
	indexTexts[slideIndex].innerHTML = (slideIndex +1) +  " / " + slides.length;
	slideIndex = (slideIndex + 1) % slides.length;

	setTimeout(showSlides, 3000);
}

}





