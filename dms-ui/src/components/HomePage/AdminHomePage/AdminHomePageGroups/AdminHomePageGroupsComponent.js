import React from "react";
import AdminHomePageGroupNavContainer from "./AdminHomePageGroupNavContainer";
import AdminHomePageGroupContainer from "./AdminHomePageGroupcontainer";

const AdminHomePageGroupsComponent = ({ username }) => {
  return (
    <div className="row full-height">
      <div className="col-lg-2 p-0 nav-color">
        <AdminHomePageGroupNavContainer />
      </div>
      <div className="col-lg-10 p-3">
        <div className="mb-5 text-center">
          <h1>Welcome, {username}</h1>
        </div>
        <AdminHomePageGroupContainer />
      </div>
    </div>
  );
};

export default AdminHomePageGroupsComponent;
