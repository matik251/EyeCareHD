using System;
using System.Collections.Generic;

namespace InventoryService.HDModels
{
    public partial class DataRecords
    {
        public int RecordId { get; set; }
        public string Mac { get; set; }
        public string Category { get; set; }
        public int Data { get; set; }
        public string Position { get; set; }
        public DateTime CreationTime { get; set; }
        public DateTime SendTime { get; set; }
        public DateTime DbTime { get; set; }
    }
}
