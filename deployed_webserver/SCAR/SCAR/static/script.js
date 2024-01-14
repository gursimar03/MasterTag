// static/script.js

function loco() {
    gsap.registerPlugin(ScrollTrigger);

    // Using Locomotive Scroll from Locomotive https://github.com/locomotivemtl/locomotive-scroll

    const locoScroll = new LocomotiveScroll({
        el: document.querySelector("#main"),
        smooth: true
    });

    // each time Locomotive Scroll updates, tell ScrollTrigger to update too (sync positioning)
    locoScroll.on("scroll", ScrollTrigger.update);

    // tell ScrollTrigger to use these proxy methods for the "#main" element since Locomotive Scroll is hijacking things
    ScrollTrigger.scrollerProxy("#main", {
        scrollTop(value) {
            return arguments.length ? locoScroll.scrollTo(value, 0, 0) : locoScroll.scroll.instance.scroll.y;
        }, // we don't have to define a scrollLeft because we're only scrolling vertically.
        getBoundingClientRect() {
            return { top: 0, left: 0, width: window.innerWidth, height: window.innerHeight };
        },
        // LocomotiveScroll handles things completely differently on mobile devices - it doesn't even transform the container at all! So to get the correct behavior and avoid jitters, we should pin things with position: fixed on mobile. We sense it by checking to see if there's a transform applied to the container (the LocomotiveScroll-controlled element).
        pinType: document.querySelector("#main").style.transform ? "transform" : "fixed"
    });

    // each time the window updates, we should refresh ScrollTrigger and then update LocomotiveScroll.
    ScrollTrigger.addEventListener("refresh", () => locoScroll.update());

    // after everything is set up, refresh() ScrollTrigger and update LocomotiveScroll because padding may have been added for pinning, etc.
    ScrollTrigger.refresh();
}

function setupTextAnimation(selector) {
    var clutter = "";
    document.querySelector(selector + ">h1").textContent.split("").forEach(function (dets) {
        clutter += `<span>${dets}</span>`;
        document.querySelector(selector + ">h1").innerHTML = clutter;
    });

    gsap.to(selector + ">h1>span", {
        scrollTrigger: {
            trigger: selector + ">h1>span",
            start: "top bottom",
            end: "bottom top",
            scroller: "#main",
            scrub: 0.5,
        },
        stagger: 0.2,
        color: "#fff",
    });
}

function canvas() {
    const canvas = document.querySelector("#page3>canvas");
    const context = canvas.getContext("2d");

    canvas.width = window.innerWidth;
    canvas.height = window.innerHeight;

    window.addEventListener("resize", function () {
        canvas.width = window.innerWidth;
        canvas.height = window.innerHeight;
        render();
    });

    function files(index) {
        var data = '';
        for (var i = 1; i <= 648; i += 5) {
            var paddedNumber = i.toString().padStart(5, '0');
            data += `${paddedNumber}.png\n`;
        }

        return data.split("\n")[index];
    }

    const frameCount = 648 / 5;

    const images = [];
    const imageSeq = {
        frame: 1,
    };

    // Use the pre-defined variables for dynamic content
    for (let i = 0; i < frameCount; i++) {
        const img = new Image();
        img.src = imageUrls[i];
        images.push(img);
    }

    gsap.to(imageSeq, {
        frame: frameCount - 1,
        snap: "frame",
        ease: "none",
        scrollTrigger: {
            scrub: 0.5,
            trigger: "#page3",
            start: "top top",
            end: "250% top",
            scroller: "#main",
        },
        onUpdate: render,
    });

    images[1].onload = render;

    function render() {
        scaleImage(images[imageSeq.frame], context);
    }

    function scaleImage(img, ctx) {
        var canvas = ctx.canvas;
        var hRatio = canvas.width / img.width;
        var vRatio = canvas.height / img.height;
        var ratio = Math.max(hRatio, vRatio);
        var centerShift_x = (canvas.width - img.width * ratio) / 2;
        var centerShift_y = (canvas.height - img.height * ratio) / 2;
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        ctx.drawImage(
            img,
            0,
            0,
            img.width,
            img.height,
            centerShift_x,
            centerShift_y,
            img.width * ratio,
            img.height * ratio
        );
    }

    ScrollTrigger.create({
        trigger: "#page3",
        pin: true,
        scroller: "#main",
        start: "top top",
        end: "250% top",
    });
}

// Call the setup functions
loco();
setupTextAnimation("#page2");
canvas();
setupTextAnimation("#page4");

