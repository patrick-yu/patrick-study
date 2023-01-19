/**
 *
 */

/*
tui.Grid.on('errorResponse', function(event){
	$component.checkAjaxError(event.xhr);
});
*/

const tuiGridTheme = {
  selection: {
    background: '#4daaf9',
    border: '#004082'
  },
  scrollbar: {
    background: '#f5f5f5',
    thumb: '#d9d9d9',
    active: '#c1c1c1'
  },
  row: {
    even: {
      //background: '#f3ffe3'
    },
    hover: {
      background: '#f3ffe3'
    }
  },
  cell: {
    normal: {
      //background: '#fbfbfb',
      border: '#e0e0e0',
      showVerticalBorder: true
    },
    header: {
      background: '#eee',
      border: '#ccc',
      showVerticalBorder: true
    },
    rowHeader: {
      //border: '#ccc',
      showVerticalBorder: true
    },
    editable: {
      background: '#fbfbfb'
    },
    selectedHeader: {
      //background: '#d8d8d8'
    },
    focused: {
      //border: '#418ed4'
    },
    disabled: {
      //text: '#b0b0b0'
    }
  }
}

//tui.Grid.applyTheme('default');
tui.Grid.applyTheme('default', tuiGridTheme);
tui.Grid.setLanguage('ko');
