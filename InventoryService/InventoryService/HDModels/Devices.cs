using System;
using System.Collections.Generic;

namespace InventoryService.HDModels
{
    public partial class Devices
    {
        public int DeviceId { get; set; }
        public string IpAddress { get; set; }
        public string Category { get; set; }
        public DateTime CreationTime { get; set; }
        public DateTime? ModTime { get; set; }
    }
}
