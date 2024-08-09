import ProfilePageAdminForm from "../ProfilePageAdminForm";
import { useAuth } from "../../contexts/AuthContext";
import ProfileIllustration from "../ProfileIllustration";
export default function ProfilePageAdmin() {
  return (
    <>
      <div className="column">
        <ProfileIllustration />
        <ProfilePageAdminForm />
      </div>
    </>
  );
}
