#!/bin/bash
set -e

STATUS=$1
EVENT=$2
REPO=$3

if [ "$STATUS" == "success" ]; then
  COLOR="#36a64f"
  MESSAGE="✅ $EVENT succeeded for $REPO"
else
  COLOR="#ff0000"
  MESSAGE="❌ $EVENT failed for $REPO"
fi

curl -X POST -H 'Content-type: application/json' \
  --data "{\"text\":\"$MESSAGE\",\"attachments\":[{\"color\":\"$COLOR\",\"text\":\"Run: ${{ github.run_id }} | Commit: ${{ github.sha }}\"}]}" \
  $SLACK_WEBHOOK_URL