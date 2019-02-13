/* The BarChart can be reused as long as the

   copyright notice is not removed:

 

   BarChart Object - Copyright 1999 InsideDHTML.com, LLC

   For more information, see www.insideDHTML.com

*/

 

function BarChart_AddValue(label, value,color ) {
            var newItem = this.valueList[this.valueList.length] = new Object
            newItem.label = label
            newItem.value = value
            newItem.color = color ? color : "black"
            return newItem
}

 

function BarChart_HTMLLabel(barItem,label,format) {
            str ="<TD nowrap width='250' valign=middle>&nbsp;" + barItem.label 
            str +=label + "&nbsp;"
            str +="</TD><td nowrap width='150' valign=middle>" + barItem.value + "</td>"

            return str
}

 

function BarChart_Draw() {
            var str = "<TABLE class='mytable'"

            if (this.bgColor!="")
                        str+=" BGCOLOR='" + this.bgColor + "'"

            str+=">"
            str+="<tr><th>Description</th><th>Number of Calls</th><th>Chart</th></tr>"

            if (this.caption.value!="") str+="<CAPTION ALIGN='" + this.caption.alignment + "'><span id='desc'>" + this.caption.value + "</span></CAPTION>"

            var totalValue = 0

            for (var i=0;i<this.valueList.length;i++) 
                        totalValue+=this.valueList[i].value

//            str+="<TD ROWSPAN=" + this.valueList.length+1 + "></TD>"

            var iPercent
            for (var i=0;i<this.valueList.length;i++) {
                str+="<TR>"
                if (this.label.orientation!="right")
                    str+=BarChart_HTMLLabel(this.valueList[i],this.unitLabel,this.label)
                        //str+="<TD ALIGN='" + this.barHOrientation + "' valign=middle>"

    			if(totalValue > 0)
                    iPercent = this.valueList[i].value/totalValue
	            else 
	              	iPercent = 0
	                	
	            var divWidth = Math.round(iPercent*this.barWidth)
	                
	            if(divWidth == 0)
	                divWidth = "5px"
	                	
                //str+="<TABLE BGCOLOR='" + this.valueList[i].color + "' CELLSPACING=0 CELLPADDING=0 HEIGHT=" + this.barHeight + " WIDTH=" + Math.round(iPercent*this.barWidth)+ "><TR><TD><div style='background-color: " + this.valueList[i].color + "'>&nbsp;</div></TD></TR></TABLE>"
                str+="<td><div style='background-color: " + this.valueList[i].color + ";width: " + divWidth + "'>&nbsp;</div>"
                str +="</TD>"

                if (this.label.orientation=="right")
                    str+=BarChart_HTMLLabel(this.valueList[i],this.unitLabel, this.label)

                str +="</TR>"
            }

            str +="</TABLE>"
            
            return str
}

 

function BarChart_SetCaption(caption, alignment, fontFace) {
            this.caption.value = caption
            this.caption.alignment = alignment ? alignment : "left"

            if (fontFace)
                        this.caption.fontFace = fontFace

            return this.caption
}

 

function BarChart_FormatLabel(orientation, fontSize, fontFace) {
            this.label.orientation = orientation

            if (fontSize!=null)
                        this.label.fontSize = fontSize

            if (fontFace)
                        this.label.fontFace = fontFace

            return this.label
}

function BarChart_GetDiv(value, totalValue) {
	if(totalValue > 0)
		iPercent = value/totalValue
	else 
		iPercent = 0
	                	
	var divWidth = Math.round(iPercent*100)
	                
	if(divWidth == 0 && value > 0)
		divWidth = "1px"
		
	//if(this.colorIdx >= 15)
    //    this.colorIdx = 0;
	    
	//var color = this.colorArray[this.colorIdx].value
	//this.colorIdx++
	var color = this.colorArray[Math.floor(Math.random()*15)].value

	str = "<div style='background-color: " + color + ";width: " + divWidth + ";float: left'>&nbsp;</div>&nbsp;&nbsp;" + value;
	
	return str
}

 

function BarChart() {
            this.label = this.caption = new Object
            this.valueList = new Array()
            this.addValue = BarChart_AddValue
            this.barWidth = 200
            this.barHeight=20
            this.bgColor=""
            this.barHOrientation = "left"
            this.formatLabel = BarChart_FormatLabel
            this.formatLabel("left","-1","Arial")
            this.setCaption = BarChart_SetCaption
            this.setCaption("","top","Arial")
            this.draw = BarChart_Draw
            this.getDiv = BarChart_GetDiv
            this.colorIdx = 0
            this.colorArray = new Array();

            this.colorArray[0] = new Object
            this.colorArray[0].value = "red";
            this.colorArray[1] = new Object
            this.colorArray[1].value = "green";
            this.colorArray[2] = new Object
            this.colorArray[2].value = "blue";
            this.colorArray[3] = new Object
            this.colorArray[3].value = "pink";
            this.colorArray[4] = new Object
            this.colorArray[4].value = "magenta";
            this.colorArray[5] = new Object
            this.colorArray[5].value = "cyan";
            this.colorArray[6] = new Object
            this.colorArray[6].value = "brown";
            this.colorArray[7] = new Object
            this.colorArray[7].value = "yellow";
            this.colorArray[8] = new Object
            this.colorArray[8].value = "purple";
            this.colorArray[9] = new Object
            this.colorArray[9].value = "black";
            this.colorArray[10] = new Object
            this.colorArray[10].value = "#1E90FF";
            this.colorArray[11] = new Object
            this.colorArray[11].value = "maroon";
            this.colorArray[12] = new Object
            this.colorArray[12].value = "olive";
            this.colorArray[13] = new Object
            this.colorArray[13].value = "#9400d3";
            this.colorArray[14] = new Object
            this.colorArray[14].value = "#ffbbff";
}
