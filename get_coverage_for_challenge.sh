#!/usr/bin/env bash

set -e
set -u
set -o pipefail

SCRIPT_CURRENT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

CHALLENGE_ID=$1
JACOCO_TEST_REPORT_XML_FILE="${SCRIPT_CURRENT_DIR}/target/scala-2.12/jacoco/report/jacoco.xml"

sbt --error clean test jacoco || true 1>&2

COVERAGE_OUTPUT=$(xmllint --xpath '//package[@name="befaster/solutions/'${CHALLENGE_ID}'"]/counter[@type="INSTRUCTION"]' ${JACOCO_TEST_REPORT_XML_FILE})
MISSED=$(echo $COVERAGE_OUTPUT | awk '{print missed, $3}' | tr '="' ' ' | awk '{print $2}')
COVERED=$(echo $COVERAGE_OUTPUT | awk '{print missed, $4}' | tr '="' ' '| awk '{print $2}')

TOTAL_LINES=$((MISSED + $COVERED))
echo $(($COVERED * 100 / $TOTAL_LINES))
