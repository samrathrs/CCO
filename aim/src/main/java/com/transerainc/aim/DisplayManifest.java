/*
 * Created on Oct 4, 2004
 *
 */
package com.transerainc.aim;


/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba </a>
 * @version $Revision: 1.1 $
 */
public class DisplayManifest {
    public static void main(String [] args) {
        Package pack = Package.getPackage("com.transerainc.aim");

        System.err.println(
            "Specification-Title            : " + pack.getSpecificationTitle());
        System.err.println(
            "Specification-Vendor           : " +
            pack.getSpecificationVendor());
        System.err.println(
            "Specification-Version          : " +
            pack.getSpecificationVersion());
        System.err.println(
            "Implementation-Title           : " +
            pack.getImplementationTitle());
        System.err.println(
            "Implementation-Vendor          : " +
            pack.getImplementationVendor());
        System.err.println(
            "Implementation-Version         : " +
            pack.getImplementationVersion());
    }
}
