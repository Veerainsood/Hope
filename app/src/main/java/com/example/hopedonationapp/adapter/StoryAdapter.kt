<?xml version="1.0" encoding="utf-8"?>
<navigation xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/auth_graph"
    app:startDestination="@id/splsh">

    <fragment
        android:id="@+id/splsh"
        android:name="com.example.hopedonationapp.authentication.Splsh"
        android:label="fragment_splsh"
        tools:layout="@layout/fragment_splsh" >
        <action
            android:id="@+id/action_splsh_to_signInFragment"
            app:destination="@id/signInFragment" />
        <action
            android:id="@+id/action_splsh_to_signInFragment2"
            app:destination="@id/signInFragment" />
        <action
            android:id="@+id/action_splsh_to_admin_entrance"
            app:destination="@id/admin_entrance" />
        <action
            android:id="@+id/action_splsh_to_signInFragment3"
            app:destination="@id/signInFragment" />
        <action
            android:id="@+id/action_splsh_to_admin_entrance2"
            app:destination="@id/admin_entrance" />
        <action
            android:id="@+id/action_splsh_to_company2"
            app:destination="@id/company" />
    </fragment>
    <fragment
        android:id="@+id/signInFragment"
        android:name="com.example.hopedonationapp.authentication.SignInFragment"
        android:label="fragment_sign_in"
        tools:layout="@layout/fragment_sign_in" >
        <action
            android:id="@+id/action_signInFragment_to_otpFragment"
            app:destination="@id/otpFragment" />
        <action
            android:id="@+id/action_signInFragment_to_otpFragment2"
            app:destination="@id/otpFragment" />
        <action
            android:id="@+id/action_signInFragment_to_otpFragment3"
            app:destination="@id/otpFragment" />
    </fragment>
    <fragment
        android:id="@+id/otpFragment"
        android:name="com.example.hopedonationapp.authentication.OTPFragment"
        android:label="fragment_otp"
        tools:layout="@layout/fragment_otp" >
        <action
            android:id="@+id/action_otpFragment_to_homeScreen"
            app:destination="@id/homeScreen" />
    </fragment>
    <fragment
        android:id="@+id/homeScreen"
        android:name="com.example.hopedonationapp.HomePage.HomeScreen"
        android:label="fragment_home_screen"
        tools:layout="@layout/fragment_home_screen" >
        <action
            android:id="@+id/action_homeScreen_to_verifiedCharityOrg"
            app:destination="@id/verifiedCharityOrg" />
        <action
            android:id="@+id/action_homeScreen_to_verifiedCharityOrg2"
            app:destination="@id/verifiedCharityOrg" />
        <action
            android:id="@+id/action_homeScreen_to_profileFragment"
            app:destination="@id/profileFragment" />
    </fragment>
    <fragment
        android:id="@+id/adminHomeFragment"
        android:name="com.example.hopedonationapp.admin.AdminHomeFragment"
        android:label="fragment_admin_home"
        tools:layout="@layout/fragment_admin_home" >
        <action
            android:id="@+id/action_adminHomeFragment_to_check_storiesFragment"
            app:destination="@id/check_storiesFragment" />
    </fragment>
    <fragment
        android:id="@+id/admin_entrance"
        android:name="com.example.hopedonationapp.activity.admin_entrance"
        android:label="fragment_admin_entrance"
        tools:layout="@layout/fragment_admin_entrance" >
        <action
            android:id="@+id/action_admin_entrance_to_adminHomeFragment"
            app:destination="@id/adminHomeFragment" />
        <action
            android:id="@+id/action_admin_entrance_to_adminHomeFragment2"
            app:destination="@id/adminHomeFragment" />
    </fragment>
    <fragment
        android:id="@+id/company"
        android:name="com.example.hopedonationapp.Comapny.Company"
        android:label="fragment_company"
        tools:layout="@layout/fragment_company" />
    <fragment
        android:id="@+id/verifiedCharityOrg"
        android:name="com.example.hopedonationapp.HomePage.VerifiedCharityOrg"
        android:label="verified_charity_org"
        tools:layout="@layout/verified_charity_org" >
        <action
            android:id="@+id/action_verifiedCharityOrg_to_payments"
            app:destination="@id/payments" />
    </fragment>
    <fragment
        android:id="@+id/payments"
        android:name="com.example.hopedonationapp.Payments"
        android:label="fragment_payments"
        tools:layout="@layout/fragment_payments" >
        <action
            android:id="@+id/action_payments_to_verifiedCharityOrg"
            app:destination="@id/verifiedCharityOrg" />
    </fragment>
    <fragment
        android:id="@+id/profileFragment"
        android:name="com.example.hopedonationapp.ProfileFragment"
        android:label="ProfileFragment" />
    <fragment
        android:id="@+id/check_storiesFragment"
        android:name="com.example.hopedonationapp.admin.check_storiesFragment"
        android:label="fragment_check_stories"
        tools:layout="@layout/fragment_check_stories" />
</navigation>
