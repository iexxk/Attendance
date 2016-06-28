package com.xuan.attendance.bean;

import java.util.List;

/**
 * Created by xuan on 2016/6/6.
 */
public class SuperOneLogin {

    /**
     * phone : 15223219684
     * name : xuan
     * email :
     * avatar : https://dn-spapi1.qbox.me/avatar/2016/06/06/rt07envekxbiznpm.jpg
     * regioncode : 86
     * persona : {"gender":"male","tags":["goodLooking"],"location":{"country":"CN","province":"","city":""},"generation":"10s","character":""}
     * group_uid : bc3a8b8945789a31921d0b224bf825a9
     * server_request_token : 8eea723abfd80e8043181da770b45765
     */

    private String phone;
    private String name;
    private String email;
    private String avatar;
    private String regioncode;
    private PersonaEntity persona;
    private String group_uid;
    private String server_request_token;

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setRegioncode(String regioncode) {
        this.regioncode = regioncode;
    }

    public void setPersona(PersonaEntity persona) {
        this.persona = persona;
    }

    public void setGroup_uid(String group_uid) {
        this.group_uid = group_uid;
    }

    public void setServer_request_token(String server_request_token) {
        this.server_request_token = server_request_token;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getRegioncode() {
        return regioncode;
    }

    public PersonaEntity getPersona() {
        return persona;
    }

    public String getGroup_uid() {
        return group_uid;
    }

    public String getServer_request_token() {
        return server_request_token;
    }

    public static class PersonaEntity {
        /**
         * gender : male
         * tags : ["goodLooking"]
         * location : {"country":"CN","province":"","city":""}
         * generation : 10s
         * character :
         */

        private String gender;
        private LocationEntity location;
        private String generation;
        private String character;
        private List<String> tags;

        public void setGender(String gender) {
            this.gender = gender;
        }

        public void setLocation(LocationEntity location) {
            this.location = location;
        }

        public void setGeneration(String generation) {
            this.generation = generation;
        }

        public void setCharacter(String character) {
            this.character = character;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }

        public String getGender() {
            return gender;
        }

        public LocationEntity getLocation() {
            return location;
        }

        public String getGeneration() {
            return generation;
        }

        public String getCharacter() {
            return character;
        }

        public List<String> getTags() {
            return tags;
        }

        public static class LocationEntity {
            /**
             * country : CN
             * province :
             * city :
             */

            private String country;
            private String province;
            private String city;

            public void setCountry(String country) {
                this.country = country;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getCountry() {
                return country;
            }

            public String getProvince() {
                return province;
            }

            public String getCity() {
                return city;
            }
        }
    }
}
