/**
 * 
 */
package com.transerainc.aim.store;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.transerainc.aim.snmp.TrapManager;
import com.transerainc.aim.system.AgentInformationManagerSystem;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public abstract class BaseStore {
	protected TrapManager trapManager;

	public BaseStore() {
		this.trapManager =
				(TrapManager) AgentInformationManagerSystem
						.findBean("trapManager");
	}

	public void writeToFile(Object obj, String filename) throws IOException {
		ObjectOutputStream ooStream =
				new ObjectOutputStream(new FileOutputStream(filename));
		ooStream.writeObject(obj);
	}

	public Object readFromFile(String filename) throws IOException,
			ClassNotFoundException {
		Object obj = null;
		File f = new File(filename);
		if (f.exists()) {
			ObjectInputStream oiStream =
					new ObjectInputStream(new FileInputStream(f));
			obj = oiStream.readObject();
		}

		return obj;
	}
}
