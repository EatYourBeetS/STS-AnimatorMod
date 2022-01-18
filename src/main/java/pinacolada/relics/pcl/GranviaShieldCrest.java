package pinacolada.relics.pcl;

import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.interfaces.subscribers.OnLosingHPSubscriber;
import eatyourbeets.powers.CombatStats;
import pinacolada.powers.PCLCombatStats;
import pinacolada.relics.PCLRelic;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.StringJoiner;

import static pinacolada.resources.GR.Enums.CardTags.PROTAGONIST;

public class GranviaShieldCrest extends PCLRelic implements OnLosingHPSubscriber
{
    public static final String ID = CreateFullID(GranviaShieldCrest.class);

    public GranviaShieldCrest()
    {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK);
    }

    @Override
    public void onEquip()
    {
        super.onEquip();
        if (tips.size() > 0)
        {
            tips.get(0).description = GetFullDescription();
        }
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        PCLCombatStats.onLosingHP.Subscribe(this);
    }

    @Override
    public int OnLosingHP(int damageAmount) {
        if (CombatStats.HasActivatedLimited(ID))
        {
            PCLCombatStats.onLosingHP.Unsubscribe(this);
            return damageAmount;
        }

        if (damageAmount > 0 && player.currentHealth <= damageAmount && CanRevive())
        {

            for (AbstractCard c : GetProtagonists()) {
                if (c != null && PCLGameUtilities.CanRemoveFromDeck(c))
                {
                    player.masterDeck.removeCard(c);
                }
                for (AbstractCard card : PCLGameUtilities.GetAllInBattleInstances(c.uuid))
                {
                    player.discardPile.removeCard(card);
                    player.drawPile.removeCard(card);
                    player.hand.removeCard(card);
                }
            }
            this.onUnequip();
            player.relics.remove(this);
            player.reorganizeRelics();

            CombatStats.TryActivateLimited(ID);
            PCLCombatStats.onLosingHP.Unsubscribe(this);
            flash();
            return 0;
        }

        return damageAmount;
    }

    private boolean CanRevive()
    {
        return PCLGameUtilities.InBattle() && PCLJUtils.Group(GetProtagonists(), c -> c.cardID).size() == 1;
    }

    private ArrayList<AbstractCard> GetProtagonists() {
        return PCLJUtils.Filter(player.masterDeck.group, c -> c.hasTag(PROTAGONIST));
    }

    public String GetFullDescription()
    {
        final HashSet<String> protagonists = new HashSet<>();
        for (AbstractCard card : GetProtagonists()) {
            if (card != null) {
                protagonists.add(card.name);
            }
        }
        StringJoiner joiner = new StringJoiner(" NL ");
        for (String s : protagonists)
        {
            joiner.add("- " + s);
        }

        return FormatDescription(0) + " NL  NL " + DESCRIPTIONS[1] + " NL " + joiner.toString();
    }
}