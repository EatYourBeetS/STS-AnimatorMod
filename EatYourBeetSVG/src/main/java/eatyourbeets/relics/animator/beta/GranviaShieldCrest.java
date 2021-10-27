package eatyourbeets.relics.animator.beta;

import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.interfaces.subscribers.OnLoseHpSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.StringJoiner;

import static eatyourbeets.resources.GR.Enums.CardTags.PROTAGONIST;

public class GranviaShieldCrest extends AnimatorRelic implements OnLoseHpSubscriber
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

        CombatStats.onLoseHp.Subscribe(this);
    }

    @Override
    public int OnLoseHp(int damageAmount) {
        if (CombatStats.HasActivatedLimited(ID))
        {
            CombatStats.onLoseHp.Unsubscribe(this);
            return damageAmount;
        }

        if (damageAmount > 0 && player.currentHealth <= damageAmount && CanRevive())
        {

            for (AbstractCard c : GetProtagonists()) {
                if (c != null && GameUtilities.CanRemoveFromDeck(c))
                {
                    player.masterDeck.removeCard(c);
                }
                for (AbstractCard card : GameUtilities.GetAllInBattleInstances(c.uuid))
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
            CombatStats.onLoseHp.Unsubscribe(this);
            flash();
            return 0;
        }

        return damageAmount;
    }

    private boolean CanRevive()
    {
        return GameUtilities.InBattle() && JUtils.Group(GetProtagonists(), c -> c.cardID).size() == 1;
    }

    private ArrayList<AbstractCard> GetProtagonists() {
        return JUtils.Filter(player.masterDeck.group, c -> c.hasTag(PROTAGONIST));
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