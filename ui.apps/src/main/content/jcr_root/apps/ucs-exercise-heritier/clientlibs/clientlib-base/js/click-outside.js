var clickOutside = (function () {
  //*********** INFO ************//
  // here there are all the elements that should be closed if click outside
  // second-lv-header | language-selector-header |

  var _clickOutside = function () {
    $(document).mouseup(function (e) {
      var w = $(window).width();

      var firstLv = $("header .first-level__item .item__text");
      var secondLv = $("header .second-level__items:visible");

      var langSelector = $("header .languages__selector");
      var langBox = langSelector.find(".selector__items");

      // Navbar Desktop - if the target of the click isn't the first-lv
      if (
        !firstLv.is(e.target) &&
        firstLv.has(e.target).length === 0 &&
        secondLv.is(":visible") &&
        w > 1024
      ) {
        secondLv.fadeOut();
        firstLv.parent().removeClass("first-level__item--opened");
      }

      // Navbar Desktop - if the target of the click isn't the language selector
      if (
        !langSelector.is(e.target) &&
        langSelector.has(e.target).length === 0 &&
        langBox.is(":visible") &&
        w > 1024
      ) {
        langBox.fadeOut();
        langSelector.find(".icon").removeClass("icon-up").addClass("icon-down");
      }
    });
  };

  var _init = function () {
    _clickOutside();
    // other functions
  };

  return {
    init: _init,
  };
})();

clickOutside.init();
