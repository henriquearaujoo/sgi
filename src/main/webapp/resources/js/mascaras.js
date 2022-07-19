//Permite somente n�meros
function somenteNumeros(numero) {

	if(document.all){ // Internet Explorer
		tecla = numero.keyCode;
	}
	else if(document.getElementById){ // Nestcape/FireFox
		tecla = numero.which;
	}
		
	//tecla==8 � para permitir o backspace funcionar para apagar
	if ( (tecla >= 48 && tecla <= 57) || tecla == 8 || tecla == 0 ) {
		return true;
	}
	else {
		return false;
	}
}

function mascaraTelefone(o,f){
    v_obj=o
    v_fun=f
    setTimeout("execmascara()",1)
}

function mascaraMoeda(o,f){ 
	v_obj=o 
	v_fun=f 
	setTimeout("execmascara()",1) 
} 

function moeda(v){ 
	v=v.replace(/\D/g,""); // permite digitar apenas numero 
	v=v.replace(/(\d{1})(\d{15})$/,"$1.$2") // coloca ponto antes dos ultimos digitos 
	v=v.replace(/(\d{1})(\d{11})$/,"$1.$2") // coloca ponto antes dos ultimos 11 digitos 
	v=v.replace(/(\d{1})(\d{8})$/,"$1.$2") // coloca ponto antes dos ultimos 8 digitos 
	v=v.replace(/(\d{1})(\d{5})$/,"$1.$2") // coloca ponto antes dos ultimos 5 digitos 
	v=v.replace(/(\d{1})(\d{1,2})$/,"$1,$2") // coloca virgula antes dos ultimos 2 digitos 
	return v; 
}

function execmascara(){
    v_obj.value=v_fun(v_obj.value)
}

function mtel(v){
    v=v.replace(/\D/g,"");             //Remove tudo o que n�o � d�gito
    v=v.replace(/^(\d{2})(\d)/g,"($1) $2"); //Coloca par�nteses em volta dos dois primeiros d�gitos
    v=v.replace(/(\d)(\d{4})$/,"$1-$2");    //Coloca h�fen entre o quarto e o quinto d�gitos
    return v;
}

function id( el ){
	return document.getElementById( el );
}

function formatarNumero(ObjForm,teclapres,tammax,decimais)
{
  var bksp = 8;
  var key_0 = 48;
  var key_9 = 57;

	if(document.all){  // Internet Explorer
    	tecla = teclapres.keyCode; 
	}
   	else if(document.getElementById){ // Nestcape
    	tecla = teclapres.which;
  	}

	var tamanhoObjeto	= ObjForm.value.length;

	if (tecla == bksp && tamanhoObjeto == tammax){
	  tamanhoObjeto = tamanhoObjeto - 1;
	}

	if ( tecla == bksp || (tecla >= key_0 && tecla <= key_9 && (tamanhoObjeto+1) <= tammax))
	//if (( tecla == bksp || tecla == 88 || tecla >= 48 && tecla <= 57 || tecla >= 96 && tecla <= 105 ) && ((tamanhoObjeto+1) <= tammax))
	{
		vr	= ObjForm.value;
		vr  = vr.replace(eval("/\\./g"),"")
		vr	= vr.replace( ",", "" );
		tam	= vr.length;

		if (tam < tammax && tecla != bksp){
			tam = vr.length + 1 ;
		}

		if ((tecla == bksp) && (tam > 1)){
			tam = tam - 1 ;
			vr = ObjForm.value;

			vr  = vr.replace(eval("/\\./g"),"")
			vr	= vr.replace( ",", "" );
		}

		//C�lculo para casas decimais setadas por parametro
		if ( tecla == bksp || (tecla >= key_0 && tecla <= key_9) )
		{
			if (decimais > 0){

				if ( (tam <= decimais) )
				{
					ObjForm.value = ("0," + vr) ;
				}
				if( (tam == (decimais + 1)) && (tecla == 8))
				{
					ObjForm.value = vr.substr( 0, (tam - decimais)) + ',' + vr.substr( tam - (decimais), tam ) ;
				}
				if ( (tam > (decimais + 1)) && (tam <= (decimais + 3)) &&  ((vr.substr(0,1)) == "0"))
				{
					ObjForm.value = vr.substr( 1, (tam - (decimais+1))) + ',' + vr.substr( tam - (decimais), tam ) ;
				}
				if ( (tam > (decimais + 1)) && (tam <= (decimais + 3)) &&  ((vr.substr(0,1)) != "0"))
				{
				    ObjForm.value = vr.substr( 0, tam - decimais ) + ',' + vr.substr( tam - decimais, tam ) ;
				}
				if ( (tam >= (decimais + 4)) && (tam <= (decimais + 6)) )
				{
			 		ObjForm.value = vr.substr( 0, tam - (decimais + 3) ) + '.' + vr.substr( tam - (decimais + 3), 3 ) + ',' + vr.substr( tam - decimais, tam ) ;
				}
			 	if ( (tam >= (decimais + 7)) && (tam <= (decimais + 9)) )
				{
			 		//ObjForm.value = vr.substr( 0, tam - (decimais + 6) ) + '.' + vr.substr( tam - (decimais + 6), 3 ) + '.' + vr.substr( tam - (decimais + 3), 3 ) + ',' + vr.substr( tam - decimais, tam ) ;
			 		teclapres.cancelBubble = true;
					return false;
				}
				if ( (tam >= (decimais + 10)) && (tam <= (decimais + 12)) )
				{
			 		ObjForm.value = vr.substr( 0, tam - (decimais + 9) ) + '.' + vr.substr( tam - (decimais + 9), 3 ) + '.' + vr.substr( tam - (decimais + 6), 3 ) + '.' + vr.substr( tam - (decimais + 3), 3 ) + ',' + vr.substr( tam - decimais, tam ) ;
				}
				if ( (tam >= (decimais + 13)) && (tam <= (decimais + 15)) )
				{
			 		ObjForm.value = vr.substr( 0, tam - (decimais + 12) ) + '.' + vr.substr( tam - (decimais + 12), 3 ) + '.' + vr.substr( tam - (decimais + 9), 3 ) + '.' + vr.substr( tam - (decimais + 6), 3 ) + '.' + vr.substr( tam - (decimais + 3), 3 ) + ',' + vr.substr( tam - decimais, tam ) ;
				}
			}
			else if(decimais == 0){
				if ( tam <= 3 )
				{
			 		ObjForm.value = vr ;
				}
				if ( (tam >= 4) && (tam <= 6) )
				{
					if(tecla == bksp)
					{
						ObjForm.value = vr.substr(0, tam);
						teclapres.cancelBubble = true;
						return false;
					}

					ObjForm.value = vr.substr(0, tam - 3) + '.' + vr.substr( tam - 3, 3 );
				}
				if ( (tam >= 7) && (tam <= 9) )
				{
					if(tecla == bksp)
					{
						ObjForm.value = vr.substr(0, tam);
						teclapres.cancelBubble = true;
						return false;
					}
					ObjForm.value = vr.substr( 0, tam - 6 ) + '.' + vr.substr( tam - 6, 3 ) + '.' + vr.substr( tam - 3, 3 );
				}
				if ( (tam >= 10) && (tam <= 12) )
				{
			 		if(tecla == bksp)
					{
						ObjForm.value = vr.substr(0, tam);
						teclapres.cancelBubble = true;
						return false;
					}
					ObjForm.value = vr.substr( 0, tam - 9 ) + '.' + vr.substr( tam - 9, 3 ) + '.' + vr.substr( tam - 6, 3 ) + '.' + vr.substr( tam - 3, 3 );
				}
				if ( (tam >= 13) && (tam <= 15) )
				{
					if(tecla == bksp)
					{
						ObjForm.value = vr.substr(0, tam);
						teclapres.cancelBubble = true;
						return false;
					}
					ObjForm.value = vr.substr( 0, tam - 12 ) + '.' + vr.substr( tam - 12, 3 ) + '.' + vr.substr( tam - 9, 3 ) + '.' + vr.substr( tam - 6, 3 ) + '.' + vr.substr( tam - 3, 3 ) ;
				}
			}
		}
	}
	else{
		if((tecla != 8) && (tecla != 9) && (tecla != 0) && (tecla != 13) && (tecla != 35) && (tecla != 36) && (tecla != 46)){
			teclapres.cancelBubble = true;
			return false;
		}
	}
}
