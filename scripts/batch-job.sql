
INSERT INTO BATCH_STEP_EXECUTION_SEQ (ID, UNIQUE_KEY) SELECT
    *
FROM
    (
        SELECT
            0 AS ID,
            '0' AS UNIQUE_KEY
    ) AS tmp
WHERE
    NOT EXISTS (
            SELECT
                *
            FROM
                BATCH_STEP_EXECUTION_SEQ
        );

INSERT INTO BATCH_JOB_EXECUTION_SEQ (ID, UNIQUE_KEY) SELECT
    *
FROM
    (
        SELECT
            0 AS ID,
            '0' AS UNIQUE_KEY
    ) AS tmp
WHERE
    NOT EXISTS (
            SELECT
                *
            FROM
                BATCH_JOB_EXECUTION_SEQ
        );

INSERT INTO BATCH_JOB_SEQ (ID, UNIQUE_KEY) SELECT
    *
FROM
    (
        SELECT
            0 AS ID,
            '0' AS UNIQUE_KEY
    ) AS tmp
WHERE
    NOT EXISTS (SELECT * FROM BATCH_JOB_SEQ);


