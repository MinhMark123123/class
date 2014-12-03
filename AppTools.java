package com.vn.apksfull.utils;

import java.util.ArrayList;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PermissionInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;

import com.vn.apksfull.object.BaseAppObject;
import com.vn.apksfull.object.ManagerAppObj;

public class AppTools {

	private Context mContext;
	public static String NORMAL_TYPE = "normal";
	public static String DANGEROUS_TYPE = "dangerous";

	public AppTools(Context mContext) {
		super();
		this.mContext = mContext;
	}

	//
	//
	public ArrayList<BaseAppObject> getAppNonSystem() {

		ArrayList<BaseAppObject> appBaselicationInfos = new ArrayList<BaseAppObject>();
		PackageManager packageManager = mContext.getPackageManager();
		ArrayList<ApplicationInfo> applicationInfos = (ArrayList<ApplicationInfo>) packageManager
				.getInstalledApplications(PackageManager.GET_META_DATA);
		for (ApplicationInfo item : applicationInfos) {
			if (packageManager.getLaunchIntentForPackage(item.packageName) != null) {
				// so if it is system app . system app couldn't lauch :)
				String nameAPP = "", packageName = "";
				nameAPP = packageManager.getApplicationLabel(item).toString();
				packageName = item.packageName;
				BaseAppObject app = new BaseAppObject();
				app.set(BaseAppObject.APP_NAME, nameAPP);
				app.set(BaseAppObject.APP_PACKAGENAME, packageName);
				appBaselicationInfos.add(app);
			}
		}
		return appBaselicationInfos;
	}

	/**
	 * this method help get information app
	 * 
	 * @return
	 */
	//
	/**
	 * 
	 */
	public ArrayList<ManagerAppObj> getAppInstalled() {
		ArrayList<ManagerAppObj> appObjs = new ArrayList<ManagerAppObj>();
		PackageManager packageManager = mContext.getPackageManager();
		ArrayList<PackageInfo> packageInfos = (ArrayList<PackageInfo>) packageManager
				.getInstalledPackages(PackageManager.SIGNATURE_MATCH);
		for (PackageInfo item : packageInfos) {
			// don't get system package
			String nameAPP = "", packageName = "", version = "", size = "", status = "";

			if (((item.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) != true) {
				Drawable imageApp = null;
				//
				nameAPP = item.applicationInfo.loadLabel(
						mContext.getPackageManager()).toString();
				packageName = item.packageName;
				version = item.versionName;
				status = getProtectionLevel(packageName);
				imageApp = item.applicationInfo.loadIcon(mContext
						.getPackageManager());
				// set value object
				ManagerAppObj app = new ManagerAppObj();
				app.set(ManagerAppObj.APP_NAME, nameAPP);
				app.set(ManagerAppObj.APP_PACKAGE_APP, packageName);
				app.set(ManagerAppObj.APP_VERSION, version);
				app.set(ManagerAppObj.APP_STATUS, status);
				app.set(ManagerAppObj.APP_SIZE, "2Mb");
				app.setImageApp(imageApp);
				// add into arraylist
				appObjs.add(app);
			}

		}
		return appObjs;
	}

	// /**
	// *
	// * @param packageName
	// */
	// public float getSizeApp(){
	//
	// }

	/**
	 * method uninstall app
	 * 
	 * @param packageName
	 *            of app to uninstall
	 */
	public void unInstallApp(String packageName) {
		Uri packageURI = Uri.parse("package:" + packageName);
		Intent intent = new Intent(Intent.ACTION_DELETE, packageURI);
		mContext.startActivity(intent);

	}
	/**
	 * get icon app
	 * @param packageName
	 * @return
	 * @throws NameNotFoundException
	 */

	public Drawable getIcon(String packageName) throws NameNotFoundException {
		return mContext.getPackageManager().getApplicationIcon(packageName);
	}
	

	public PermissionInfo[] getAppPermission(String packageName)
			throws NameNotFoundException {
		PackageManager manager = mContext.getPackageManager();
		// ArrayList<PackageInfo> packageInfos = (ArrayList<PackageInfo>)
		// manager
		// .getInstalledPackages(PackageManager.GET_PERMISSIONS);
		// for (int i = 0; i < packageInfos.size(); i++) {
		// if (packageInfos.get(i).packageName.equals(packageName)) {
		// return packageInfos.get(i).permissions;
		// }
		// }
		PackageInfo packageInfo = manager.getPackageInfo(packageName,
				PackageManager.GET_PERMISSIONS);
		if (packageInfo.permissions != null) {
			return packageInfo.permissions;
		}
		return null;
	}

	// /**
	// *
	// * @param permissionInfo
	// * @return
	// */
	// public String checkProtectionLevel(PermissionInfo permissionInfo) {
	// String protectionLevel = "";
	// if (permissionInfo != null) {
	// switch (permissionInfo.protectionLevel) {
	// case PermissionInfo.PROTECTION_NORMAL:
	// protectionLevel = "Binh thuong";
	// break;
	// case PermissionInfo.PROTECTION_DANGEROUS:
	// protectionLevel = "Nguy hiem";
	// break;
	// case PermissionInfo.PROTECTION_SIGNATURE:
	// protectionLevel = "Dang ki";
	// break;
	// case PermissionInfo.PROTECTION_SIGNATURE_OR_SYSTEM:
	// protectionLevel = "He thong hoac dang ki";
	// break;
	// default:
	// protectionLevel = " <unknow>";
	// break;
	// }
	// }
	// return protectionLevel;
	// }
	/**
	 * get protectionlevel permission app
	 * @param packageName
	 * @return String 
	 */
	public String getProtectionLevel(String packageName) {
		String[] permisisonNameArrays = getPermissionName(packageName);
		Permission permission = new Permission(mContext);
		if(permisisonNameArrays != null){			
			for (String item : permisisonNameArrays) {
				if (permission.isDangerousPermisison(item)) {
					return DANGEROUS_TYPE;
				}
			}
		}
		
		return NORMAL_TYPE;
	}

	// /**
	// * get protection level app
	// * @param packageName
	// * @return
	// */
	// public String protectionLevel(String packageName) {
	//
	// //PackageInfo info;
	// try {
	// //info = mContext.getPackageManager().getPackageInfo(packageName,
	// PackageManager.GET_PROVIDERS);
	// PermissionInfo[] permissionInfos =
	// getAppPermission(packageName);//info.permissions;
	// if (permissionInfos != null) {
	// Log.d("MINH STARK", "[AppTools]- permissioninfo  : " + permissionInfos);
	// for (PermissionInfo item : permissionInfos) {
	// Log.d("MINH STARK", "[AppTools]-protectionLevel item : " + item);
	// if (!checkProtectionLevel(item).equals("Binh thuong")) {
	// return "Nguy hiem";
	// }
	// }
	// }
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// return "Binh thuong";
	// }

	/**
	 * get versionName in Manifest app
	 * @param packageName app want to get versionName
	 * @return String versionname
	 */
	public String getVersionName(String packageName) {
		String versionCode = "";
		PackageManager packageManager = mContext.getPackageManager();
		try {
			PackageInfo pageInfo = packageManager
					.getPackageInfo(packageName, 0);
			versionCode = pageInfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return versionCode;
	}

	/**
	 * get permission app 
	 * @param packageName app want to get permission
	 * @return String[] permission
	 */
	public String[] getPermissionName(String packageName) {
		PackageManager manager = mContext.getPackageManager();
		ArrayList<PackageInfo> packageInfos = (ArrayList<PackageInfo>) manager
				.getInstalledPackages(PackageManager.GET_PERMISSIONS);
		for (int i = 0; i < packageInfos.size(); i++) {
			if (packageInfos.get(i).packageName.equals(packageName)) {
				return packageInfos.get(i).requestedPermissions;
			}
		}
		return null;
	}

	/**
	 * get permisison app detail infor
	 * 
	 * @param packageName
	 * @return arraylist string permission app
	 * @throws NameNotFoundException
	 */
	public ArrayList<String> getPermissionAppDetail(String packageName)
			throws NameNotFoundException {
		ArrayList<String> mListPermission = new ArrayList<String>();
		PermissionInfo[] permissionInfos = getAppPermission(packageName);
		String[] permissionName = getPermissionName(packageName);
		// String requetsPermision = permissionInfos
		// for(PermissionInfo item : permissionInfos){
		// if(!checkProtectionLevel(item).equals("Binh thuong")){
		// //String[] arrayPermission =
		// }
		// }
		Permission permission = new Permission(mContext);
		for (int i = 0; i < permissionName.length; i++) {
			if (permission.isDangerousPermisison(permissionName[i])) {				
				String[] arrayTemp = ((permissionName[i])).split(Pattern
						.quote("."));
				String namePermisison = arrayTemp[arrayTemp.length - 1];
				String mString = permission.getPermissionDetail(namePermisison);
				mListPermission.add(mString);
			}
			// }
		}
		return mListPermission;
	}
}
