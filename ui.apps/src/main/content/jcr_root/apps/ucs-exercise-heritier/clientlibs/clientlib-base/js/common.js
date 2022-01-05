// universal code

$(document).ready(function () {
  dataNumberSwiper();
  dataOrderSwiper();
  // ZOOM TEST BEGIN
  $(".zoom--80").click(function () {
    document.body.style.zoom = "80%";
  });
  $(".zoom--90").click(function () {
    document.body.style.zoom = "90%";
  });
  $(".zoom--100").click(function () {
    document.body.style.zoom = "100%";
  });
  $(".zoom--150").click(function () {
    document.body.style.zoom = "150%";
  });
  $(".apply__customZoom").click(function () {
    document.body.style.zoom = $(".zoom--custom").val() + "%";
  });
  // ZOOM TEST END
});

$(window).on("load", function () {
  var w = $(window).width();
  addBg(w);
});

$(window).on("resize", function () {
  var w = $(window).width();
  resetImg();
  addBg(w);
});

// Extra Hashtag logic
function extraHash(container) {
  container.find(".hashTag").show();
  container.find(".more-hashtag").hide();
  container.map(function () {
    var sumHashtag = 0;
    var sumHashtagExtra = 0;
    // 1) Get width of all Hashtag with margin and padding
    $(this)
      .find(".hashTag")
      .map(function () {
        sumHashtag = sumHashtag + $(this).outerWidth();
      });
    // 2) Check if hashtag parent has enough space
    if (sumHashtag < $(this).find(".hashTags").width() - 20) {
      // in this case the extra symbol will not be added
    } else {
      // 3) Calculate how to insert the extra symbol
      var hashtagDiv = $(this).find(".hashTags").width() - 70;
      $(this)
        .find(".hashTag")
        .map(function () {
          sumHashtagExtra = sumHashtagExtra + $(this).outerWidth();
          if (sumHashtagExtra > hashtagDiv) {
            $(this).hide();
          }
        });
      $(this).find(".more-hashtag").css("display", "flex");
    }
  });
}

// Check if element is visibile into viewport
function isOnScreen(elem) {
  // if the element doesn't exist, abort
  if (elem.length == 0) {
    return;
  }
  var $window = jQuery(window);
  var viewport_top = $window.scrollTop();
  var viewport_height = $window.height();
  var viewport_bottom = viewport_top + viewport_height;
  var $elem = jQuery(elem);
  var top = $elem.offset().top;
  var height = $elem.height();
  var bottom = top + height;

  return (
    (top >= viewport_top && top < viewport_bottom) ||
    (bottom > viewport_top && bottom <= viewport_bottom) ||
    (height > viewport_height &&
      top <= viewport_top &&
      bottom >= viewport_bottom)
  );
}

var resetImg = function () {
  $("[data-bg], [data-bg-mobile]").each(function () {
    $(this).css("background-image", ""); // reset all bg
  });
};

var addBg = function (w) {
  if (w > 1024) {
    resetImg();
    $("[data-bg]").each(function () {
      var imageUrl = $(this).attr("data-bg");
      if (typeof imageUrl !== typeof undefined && imageUrl !== false) {
        $(this).css("background-image", 'url("' + imageUrl + '")');
      }
    });
  } else if (w <= 1024) {
    resetImg();
    $("[data-bg-mobile]").each(function () {
      var imageUrl = $(this).attr("data-bg-mobile");
      if (typeof imageUrl !== typeof undefined && imageUrl !== false) {
        $(this).css("background-image", 'url("' + imageUrl + '")');
      }
    });
  }
};

var dataNumberSwiper = function () {
  $(".swiper-container").each(function () {
    var slide = $(this).find(".swiper-slide:not(.swiper-slide-duplicate)");
    $(this).find("[data-slide-number]").removeAttr("data-slide-number"); // remove all
    slide.each(function (index) {
      $(this).attr("data-slide-number", index + 1);
    });
  });
};

var dataOrderSwiper = function () {
  $(".swiper-container").each(function () {
    var slide = $(this).find(".swiper-slide:not(.swiper-slide-duplicate)");
    slide.closest(".swiper-wrapper").attr("data-order-main", "");
    slide.each(function (index) {
      $(this).attr("data-order", index + 1);
    });
  });
};

var reorderSwiper = function (el) {
  var tempReorder = [];

  el.each(function () {
    var $this = $(this);
    var order = $this.find("[data-order]");

    order.each(function () {
      tempReorder.push($(this).attr("data-order"));
    });

    tempReorder.sort(function (a, b) {
      return a - b;
    });
    tempReorder.forEach(function (item) {
      var text = $this.find('[data-order="' + item + '"]').get();
      $this.append(text);
    });

    tempReorder = []; // reset
  });
};

$(document).on("click", "[data-video] .player__start", function (e) {
  e.preventDefault();
  var url = $(this).closest("[data-video]").attr("data-video");
  $(".videoModal").remove();
  var w = $(window).width();
  var height;

  if (w > 1024) {
    height = "100%";
  } else {
    height = "auto";
  }

  if (url != undefined) {
    $("body, html").css("overflow", "hidden");
    $("body").append(
      '<div class="videoModal ucg-icons__newPackage">' +
        '<div class="videoModal__close icon-close"></div>' +
        '<iframe width="100%" height="' +
        height +
        '" src="' +
        url +
        '?autoplay=1&cc_load_policy=1&muted=1"' +
        'frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe></div>'
    );

    console.log(url);
    setTimeout(function () {
      $(".videoModal").addClass("videoModal--show");
    }, 200);
  }
});

$(document).on("click", ".videoModal .videoModal__close", function (e) {
  e.preventDefault();
  $(".videoModal").removeClass("videoModal--show");
  $("body, html").css("overflow", "");

  setTimeout(function () {
    $(".videoModal").remove();
  }, 200);
});

/************************* DATE INPUT */

$(document).on("keyup", '[name="dateField"]', function (evt) {
  var $this = $(this);
  if (
    (evt.keyCode >= 48 && evt.keyCode <= 57) ||
    (evt.keyCode >= 96 && evt.keyCode <= 105)
  ) {
    evt = evt || window.event;

    var size = $this.val().length;

    if (
      (size == 2 && $this.val() > 31) ||
      (size == 5 && Number($this.val().split("-")[1]) > 12) ||
      (size >= 10 && Number($this.val().split("-")[2]) > 2200)
    ) {
      $this.val($this.val() + "-");
      return;
    }

    if (
      (size == 2 && $this.val() < 32) ||
      (size == 5 && Number($this.val().split("-")[1]) < 13)
    ) {
      $this.val($this.val() + "-");
    }
  } else {
    /* document.getElementById('dateField').value = ''; */
  }
});
