using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using InventoryService.HDModels;

namespace InventoryService.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class DataRecordsController : ControllerBase
    {
        private readonly HDContext _context;

        public DataRecordsController(HDContext context)
        {
            _context = context;
        }

        // GET: api/DataRecords
        [HttpGet]
        public async Task<ActionResult<IEnumerable<DataRecords>>> GetDataRecords()
        {
            return await _context.DataRecords.ToListAsync();
        }

        // GET: api/DataRecords/5
        [HttpGet("{id}")]
        public async Task<ActionResult<DataRecords>> GetDataRecords(int id)
        {
            var dataRecords = await _context.DataRecords.FindAsync(id);

            if (dataRecords == null)
            {
                return NotFound();
            }

            return dataRecords;
        }

        // PUT: api/DataRecords/5
        // To protect from overposting attacks, enable the specific properties you want to bind to, for
        // more details, see https://go.microsoft.com/fwlink/?linkid=2123754.
        [HttpPut("{id}")]
        public async Task<IActionResult> PutDataRecords(int id, DataRecords dataRecords)
        {
            if (id != dataRecords.RecordId)
            {
                return BadRequest();
            }

            _context.Entry(dataRecords).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!DataRecordsExists(id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return NoContent();
        }

        // POST: api/DataRecords
        // To protect from overposting attacks, enable the specific properties you want to bind to, for
        // more details, see https://go.microsoft.com/fwlink/?linkid=2123754.
        [HttpPost]
        public async Task<ActionResult<DataRecords>> PostDataRecords(DataRecords dataRecords)
        {
            _context.DataRecords.Add(dataRecords);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetDataRecords", new { id = dataRecords.RecordId }, dataRecords);
        }

        // DELETE: api/DataRecords/5
        [HttpDelete("{id}")]
        public async Task<ActionResult<DataRecords>> DeleteDataRecords(int id)
        {
            var dataRecords = await _context.DataRecords.FindAsync(id);
            if (dataRecords == null)
            {
                return NotFound();
            }

            _context.DataRecords.Remove(dataRecords);
            await _context.SaveChangesAsync();

            return dataRecords;
        }

        private bool DataRecordsExists(int id)
        {
            return _context.DataRecords.Any(e => e.RecordId == id);
        }
    }
}
