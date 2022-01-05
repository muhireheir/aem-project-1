$(document).ready(function () {
  /****************************************************
   * MAIN
   ****************************************************/

  // DOM DATA
  var navBar = $(".navbar");

  var menuZeroLv = $(".navbar__zero-level");

  var LinkFirstLv = $(".navbar .first-level__item");
  var LinksFirstLv = $(".navbar .first-level__items");

  var LinkSecondLv = $(".navbar .second-level__item");
  var LinksSecondLv = $(".navbar .second-level__items");
  var menuSecondLv = $(".navbar__second-level");
  var secondLvTitle = $(".second-level__item--title");

  var langSelector = $(".navbar .languages__selector");
  var langItems = $(".languages__selector .selector__items");
  var langItem = $(".selector__items .selector__item");
  var langText = $(".languages__selector .selector__text");

  var hamburger = $(".hamburger");
  var hamburgerMenu = $(".navbar__hamburger");
  var backHamburger = $(".navbar__back");

  /* NAV FIRST & SECOND LEVEL
  ============================= */

  // First Level Click
  LinkFirstLv.click(function (event) {
    var link = $(this).attr("data-nav-1");

    if (
      !navBar.hasClass("mobile") &&
      !$(this).hasClass("first-level__item--opened")
    ) {
      LinksSecondLv.hide();
      navBar
        .find('.second-level__items[data-nav-2 ="' + link + '"]')
        .css("display", "flex")
        .hide()
        .fadeIn();
      LinkFirstLv.removeClass("first-level__item--opened");
      $(this).addClass("first-level__item--opened");
    } else if ($(this).hasClass("first-level__item--opened")) {
      navBar.find('.second-level__items[data-nav-2 ="' + link + '"]').fadeOut();
      LinkFirstLv.removeClass("first-level__item--opened");
    }

    if (hamburgerMenu.hasClass("navbar__hamburger--open")) {
      event.preventDefault();
      menuZeroLv.hide();

      menuSecondLv.addClass("navbar__second-level--open");
      menuSecondLv.css("display", "flex").hide().fadeIn(300);
      backHamburger.css("display", "flex").hide().fadeIn(300);
      LinksSecondLv.hide();
      LinksFirstLv.hide();
      LinkFirstLv.hide();

      navBar
        .find('.second-level__items[data-nav-2 ="' + link + '"]')
        .find(".second-level__item--title")
        .show();
      navBar.find('.second-level__items[data-nav-2 ="' + link + '"]').show();
    }
  });

  // Second Level Hover DESKTOP
  LinkSecondLv.hover(function () {
    if (!navBar.hasClass("mobile")) {
      $(this).toggleClass("second-level__item--active");
    }
  });

  $(".second-level__item--title").hover(function () {
    if (!navBar.hasClass("mobile")) {
      $(this).toggleClass("second-level__item--active");
    }
  });

  // Second Level Back Button MOBILE
  backHamburger.click(function () {
    menuSecondLv.removeClass("navbar__second-level--open");
    menuSecondLv.fadeOut(300);
    backHamburger.fadeOut(300);
    LinksSecondLv.fadeOut(300);
    secondLvTitle.fadeOut(300);
    menuZeroLv.css("display", "flex").hide().fadeIn(300);
    LinkFirstLv.css("display", "flex");
    LinksFirstLv.css("display", "flex").hide().fadeIn(300);
  });

  //Set Lang Prefix initially
  ChangeLangText();

  // Click Language Selector DESKTOP
  langSelector.click(function () {
    if (!navBar.hasClass("mobile")) {
      langItems.fadeToggle();
      $(this).children(".icon").toggleClass("icon-up");
    }
  });

  // Hover on Language DESKTOP and MOBILE
  langItem.hover(function () {
    if (!$(this).hasClass("selector__item--disabled")) {
      langItem.removeClass("selector__item--active");
      $(this).addClass("selector__item--active");
    }
  });

  // Click On Language DESKTOP and MOBILE
  langItem.click(function () {
    var langSelected = $(this).attr("data-lang");
    if (!$(this).hasClass("selector__item--disabled")) {
      langText.text(langSelected);
    }
  });

  /* HAMBURGER-MENU
  ============================= */
  //Click On Hamburge Icon
  hamburger.click(function () {
    if (!hamburgerMenu.hasClass("navbar__hamburger--open")) {
      hamburgerMenu.css("display", "flex");
      $("body, html").css({ height: "100vh", overflow: "hidden" });
      hamburger.addClass("hamburger--opened");

      if (menuSecondLv.hasClass("navbar__second-level--open")) {
        menuZeroLv.hide();
      }
      hamburgerMenu.animate({ left: "0" }, 300, function () {
        navBar.toggleClass("navbar--open");
        hamburgerMenu.toggleClass("navbar__hamburger--open");
        heightOnMobile();
      });
    } else {
      $('.navbar').css('min-height','');
      $("body, html").css({ height: "", overflow: "" });
      navBar.toggleClass("navbar--open");
      hamburgerMenu.toggleClass("navbar__hamburger--open");
      menuZeroLv.css("display", "flex");
      hamburger.removeClass("hamburger--opened");
      hamburgerMenu.animate({ left: "2000" }, 300, function () {
        hamburgerMenu.hide();
      });
    }
  });
});

/* LANGUAGES SELECTOR
============================= */

//Get languages list
function getLanguage() {
  var totalLang = $(".selector__items");
  var langList = [];
  totalLang.children(".selector__item").each(function () {
    if ($(window).width() > 1024) {
      langList.push($(this).attr('data-lang-dk'));
    } else {
      langList.push($(this).attr('data-lang-m'));
    }
  });
  return langList;
}

/****************************************************
 * FUNCTIONS
 ****************************************************/
//Change Lang Prefix depending on resolution

function ChangeLangText() {
  if ($(this).width() < 1025) {
    $('.selector__items').attr('style', '');
    $(".navbar").addClass("mobile");
    $(".selector__items").children(".selector__item").each(function (index) {
      langPrefix = $(this).text().substring(0, 2);
      langHtml = langPrefix + "<span> / </span>";
      if (index !== $(".selector__items").children().length - 1) {
        $(this).html(langHtml);
      } else {
        $(this).html(langPrefix);
      }
    });
  } else {
    $(".navbar").removeClass("mobile");
    $(".selector__items").children(".selector__item").each(function (index) {
      $(this).text(getLanguage()[index]);
    });
  }
}

/* RESOLUTION RESIZE
============================= */

function heightOnMobile() {
  var w = $(window).width();
  var el1 = $(".navbar .first-level__items");
  var el2 = $(".navbar .second-level__items");
  if (w <= 1024) {
    var headerH = $(window).outerHeight();
    $(".navbar--open").css("min-height", '');
    $(".navbar__hamburger--open").css("min-height", '');
    if ($(".navbar").is(".navbar--open") && $(".navbar__hamburger").is(".navbar__hamburger--open")) {
      $(".navbar--open").css("min-height", headerH);
      $(".navbar__hamburger--open").css("min-height", headerH);
    }

    el1.css({ "max-height": headerH - 250, "max-width": w - 45 });
    el2.css({ "max-height": headerH - 50, "max-width": w - 15 });
  }
}

$(window).on("load resize", function () {
  var w = $(window).width();
  if (w > 1024) {
    // close menu and reset options
    $(".navbar__hamburger").removeClass('navbar__hamburger--open').attr('style', '');
    $('.first-level__items, .second-level__items').attr('style', '');
    $('.second-level__items').hide();
    $('.navbar').removeClass('mobile navbar--open');
    $("body, html").css({ height: "", overflow: "" });
    $(".navbar").removeClass("mobile").css('min-height', '');
    $(".navbar__hamburger--open").css("min-height", '');
    // be sure that is hide
  } else {
    if (!$(".navbar").is('.mobile')) {
      $(".navbar").addClass("mobile");
    }
    $(".navbar").css('min-height', '');
  }

  setTimeout(function () {
    heightOnMobile();
  }, 300);

  ChangeLangText();

});
