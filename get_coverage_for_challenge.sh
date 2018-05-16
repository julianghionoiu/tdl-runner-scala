#!/usr/bin/env bash

set -e
set -u
set -o pipefail

SCRIPT_CURRENT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

CHALLENGE_ID=$1
JACOCO_TEST_REPORT_XML_FILE="${SCRIPT_CURRENT_DIR}/target/scala-2.12/jacoco/report/jacoco.xml"

sbt --error clean jacoco || true 1>&2

#
# TODO: when one or more tests fails the jacoco coverage xml isn't created so we return 0.
#       This is a work-around in case of missing coverage xml file.
#       Proper solution would be to capture the results of the failing task and do nothing and proceed with execution
#       Something to pick up at a later stage.
#
if [ -f "${JACOCO_TEST_REPORT_XML_FILE}" ]; then
    COVERAGE_OUTPUT=$(xmllint --xpath '//package[@name="befaster/solutions/'${CHALLENGE_ID}'"]/counter[@type="INSTRUCTION"]' ${JACOCO_TEST_REPORT_XML_FILE})
    MISSED=$(echo $COVERAGE_OUTPUT | awk '{print missed, $3}' | tr '="' ' ' | awk '{print $2}')
    COVERED=$(echo $COVERAGE_OUTPUT | awk '{print missed, $4}' | tr '="' ' '| awk '{print $2}')

    TOTAL_LINES=$((MISSED + $COVERED))
    echo $(($COVERED * 100 / $TOTAL_LINES))
else
    echo 0
fi
