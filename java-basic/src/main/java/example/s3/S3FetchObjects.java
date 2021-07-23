package example.s3;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

public class S3FetchObjects {

	public static void s3GetObject() throws IOException {
		final String USAGE = "\n" + "Usage:\n" + "    GetObjectData <bucketName> <keyName> <path>\n\n" + "Where:\n"
				+ "    bucketName - the Amazon S3 bucket name. \n\n" + "    keyName - the key name. \n\n"
				+ "    path - the path where the file is written to. \n\n";
System.out.println("************S3 fetch object call initiating******");
		try {
			String bucketName = "jayaedu1";
			String keyName = "linux.pem";
			String key2="ssh-keyscan";
			String path = "/tmp/linux.pem";
			String path2 = "/tmp/ssh-keyscan";
			String key3="known_hosts";
			String path3 = "/tmp/known_hosts";

			Region region = Region.US_EAST_1;
			S3Client s3 = S3Client.builder().region(region).build();

			getObjectBytes(s3, bucketName, keyName, path);
			getObjectBytes(s3, bucketName, key2, path2);
			getObjectBytes(s3, bucketName, key3, path3);
			s3.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
			// System.exit(1);
		}

	}

	public static void getObjectBytes(S3Client s3, String bucketName, String keyName, String path) throws IOException {

		try {
			GetObjectRequest objectRequest = GetObjectRequest.builder().key(keyName).bucket(bucketName).build();

			ResponseBytes<GetObjectResponse> objectBytes = s3.getObjectAsBytes(objectRequest);
			byte[] data = objectBytes.asByteArray();

			// Write the data to a local file
			File myFile = new File(path);
			OutputStream os = new FileOutputStream(myFile);
			os.write(data);
			System.out.println("Successfully obtained bytes from an S3 object");
			os.close();

		} catch (IOException ex) {
			ex.printStackTrace();
			throw ex;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
			// System.exit(1);
		}
		// snippet-end:[s3.java2.getobjectdata.main]
	}

}
