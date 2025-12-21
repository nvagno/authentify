create table if not exists "user" (
    id varchar primary key,
    email varchar,
    password_hash varchar,
    otp_validation_required boolean default true,
    otp_secret bytea default null,
    created_at timestamp with time zone
)