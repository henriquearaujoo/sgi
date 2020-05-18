package util;

public class SenhaUtil {

	
	private static final int TAMANHO_MINIMO = 4;
	private static final int TAMANHO_MAXIMO = 14;
	private static final int QUANTIDADE_DIGITOS = 2;
	private static final int QUANTIDADE_LETRAS = 2;
	
	
	
//	private List<String> checkPasswordRules(String senha) throws Exception{
//		if (StringUtils.isBlank(senha)){
//			throw new Exception("Senha deve ser informada");
//		}
//		
//		// regra de tamanho mínimo/máximo
//		LengthRule lr = new LengthRule(TAMANHO_MINIMO, TAMANHO_MAXIMO);
//		
//		// regra de espaços não permitidos
//		WhitespaceRule wr = new WhitespaceRule();
//		
//		// regra de caracter alfabético obrigatório
//		AlphabeticalCharacterRule  ac = new AlphabeticalCharacterRule (QUANTIDADE_LETRAS); 
//		
//		// regra de dígitos obrigatórios
//		DigitCharacterRule dc = new DigitCharacterRule(QUANTIDADE_DIGITOS);
//		
//		// regra de caracteres especiais obrigatórios
//		SpecialCharacterRule  nac = new SpecialCharacterRule ();
//		
//		// regra de caracter maiúsculo obrigatório
//		UppercaseCharacterRule uc = new UppercaseCharacterRule();
//		
//		List<Rule> ruleList = new ArrayList<Rule>();
//		ruleList.add(lr);
//		ruleList.add(wr);
//		ruleList.add(ac);
//		ruleList.add(dc);
//		ruleList.add(nac);
//		ruleList.add(uc);
//		
//		Properties props = new Properties();
//		props.load(new FileInputStream("./src/main/resources/messages.properties"));
//		MessageResolver resolver = new PropertiesMessageResolver(props);
//		
//		PasswordValidator validator = new PasswordValidator(resolver, ruleList);
//		PasswordData passwordData = new PasswordData(new String(senha));
//
//		RuleResult result = validator.validate(passwordData);
//		if (!result.isValid()) {
//		  return validator.getMessages(result);
//		}
//		return null;
//	}
}
