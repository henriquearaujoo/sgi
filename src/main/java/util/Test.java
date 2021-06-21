package util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Test {

	public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		
		
		
		
		MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
		byte messageDigest[] = algorithm.digest("teste".getBytes("UTF-8"));
		 
		StringBuilder hexString = new StringBuilder();
		for (byte b : messageDigest) {
		  hexString.append(String.format("%02X", 0xFF & b));
		}
		
		
		
		String senha = hexString.toString();
		
		
		
		 /* ItemCompra itemCompra = new ItemCompra();
		  
		  ItemCompra item = new ItemCompra();
		  item.setValorDesconto(BigDecimal.valueOf(10));
		  item.setValor(BigDecimal.valueOf(15));
		  item.setQuantidade(Double.valueOf(3));
		  
		  BigDecimal total1 = BigDecimal.ZERO, desc1 = BigDecimal.ZERO;
		  
		  itemCompra.setP1(item.getValorDesconto());
		  itemCompra.setT1(BigDecimal.valueOf(item.getQuantidade()).multiply(item.getValorDesconto()));
		  total1 = total1.add(itemCompra.getT1());
		  desc1 = desc1.add(BigDecimal.valueOf(item.getQuantidade()).multiply(item.getValor()).subtract(itemCompra.getT1()) );
		
		
		  
		  item.setValorDesconto(BigDecimal.valueOf(8));
		  item.setValor(BigDecimal.valueOf(10));
		  item.setQuantidade(Double.valueOf(2));
		  
		  itemCompra.setP1(item.getValorDesconto());
		  itemCompra.setT1(BigDecimal.valueOf(item.getQuantidade()).multiply(item.getValorDesconto()));
		  total1 = total1.add(itemCompra.getT1());
		  desc1 = desc1.add(BigDecimal.valueOf(item.getQuantidade()).multiply(item.getValor()).subtract(itemCompra.getT1()) );  
		
		  
		  
		/*StringBuilder s = new StringBuilder("SC");
		
		Calendar calendar =  Calendar.getInstance();
		int ano = calendar.get(Calendar.YEAR);
		int mes = calendar.get(Calendar.MONTH) + 1;
		int dia =  calendar.get(Calendar.DAY_OF_MONTH);
		int minuto = calendar.get(Calendar.MINUTE);
		int segundo = calendar.get(Calendar.SECOND);
		int milisegundo = calendar.get(Calendar.MILLISECOND);

		
		s.append(String.valueOf(ano).substring(2));
		s.append(String.valueOf(mes));
		s.append(String.valueOf(dia));
		s.append(String.valueOf(minuto));
		s.append(String.valueOf(segundo));
		s.append(String.valueOf(milisegundo).substring(0, 2));
		*/
		
		
		
		
	}

}
