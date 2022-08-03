import java.io.*;
import java.net.*;
import java.util.*;

public class Main {
	public static long depth = 500;
	public static Set<URL> checked = new HashSet<>();
	public static File urls_txt = new File("urls.txt");

	public static void main(String[] args) throws IOException {
		// https://www.lcps.org/domain/11777
		Scanner scan = new Scanner(System.in);

		System.out.println("where would you like to start your web crawler? (enter a valid URL)");
		String url = scan.nextLine();

		urls_txt.delete();

		URL link = new URL(url);

		System.out.printf("visited %d urls. you can view them in urls.txt", getLinks(link));
	}

	public static int getLinksHelper(Object[] list, int subtotal) throws IOException {

		int total = subtotal;
		if (urls_txt.length() > depth)
			return total;

		if (subtotal == 0)
			return 0;

		for (int i = 0; i < list.length; i++) {

			total += getLinks((URL) list[i]);
		}

		return total;
	}

	public static int getLinks(URL parent) throws IOException {
		Set<URL> urls = new HashSet<>();
		try {
			try (

					DataInputStream buff = new DataInputStream(new BufferedInputStream(parent.openStream()));

					DataOutputStream urlsWriter = new DataOutputStream(
							new BufferedOutputStream(new FileOutputStream("urls.txt", true)));

			) {
				String s = "";
				boolean b = false;

				for (int i = 0; i < 1000; i++) {
					for (int j = 0; j < 50; j++) {
						s = s + ((char) buff.read());
					}
					if (b) {
						URL site = new URL(s);
						for (int k = s.length(); k > 0; k--) {

							try {
								site = new URL(s.substring(0, k));

								if (new File(site.getFile()).length() > 1L) {
									break;

								}

							} catch (MalformedURLException e) {

							}

						}

						if (!checked.contains(site)) {
							urlsWriter.writeUTF(site.toString());
							checked.add(site);
							urls.add(site);
						}
						s = "";
						b = false;
					}

					if (s.contains("https:/")) {
						s = s.substring(s.indexOf("https"));
						b = true;
						continue;
					}
					s = "";
				}

			}
		} catch (FileNotFoundException e) {

			return getLinksHelper(urls.toArray(), urls.size());
		} catch (IOException e) {

			return getLinksHelper(urls.toArray(), urls.size());
		}

		return getLinksHelper(urls.toArray(), urls.size());
	}
}