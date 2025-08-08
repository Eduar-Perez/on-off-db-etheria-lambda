package main;

import java.util.Map;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rds.RdsClient;
import software.amazon.awssdk.services.rds.model.StartDbInstanceRequest;
import software.amazon.awssdk.services.rds.model.StopDbInstanceRequest;

public class OnOffDbHandler implements RequestHandler<Map<String, String>, String> {

	private final RdsClient rdsClient;

	public OnOffDbHandler() {
		this.rdsClient = RdsClient.builder()
				.region(Region.of(System.getenv("AWS_REGION")))
				.credentialsProvider(EnvironmentVariableCredentialsProvider.create())
				.build();
	}

	@Override
	public String handleRequest(Map<String, String> input, Context context) {
		String action = input.get("action");
		String dbInstanceIdentifier = System.getenv("DB_INSTANCE_IDENTIFIER");

		try {
			if(action.equalsIgnoreCase("start")) {
				rdsClient.startDBInstance(StartDbInstanceRequest.builder()
						.dbInstanceIdentifier(dbInstanceIdentifier)
						.build());

				return "La base de datos etheria se ha encendido";
			} else if (action.equalsIgnoreCase("stop")) {
				rdsClient.stopDBInstance(StopDbInstanceRequest.builder()
						.dbInstanceIdentifier(dbInstanceIdentifier)
						.build());

				return "La base de datos etheria se ha apagado";
			} else {
				return "Acción no reconocida";
			}
		} catch (Exception e) {
			context.getLogger().log("Error: " + e.getMessage());
			return "Error al procesar la solicitud. ";
		}
	}



}
