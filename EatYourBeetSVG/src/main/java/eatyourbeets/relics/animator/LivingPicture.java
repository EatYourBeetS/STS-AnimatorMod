package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import eatyourbeets.cards.animator.enchantments.Enchantment;
import eatyourbeets.cards.animator.enchantments.Enchantment1;
import eatyourbeets.cards.animator.enchantments.Enchantment2;
import eatyourbeets.cards.animator.enchantments.Enchantment3;
import eatyourbeets.interfaces.subscribers.OnSynergySubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class LivingPicture extends AnimatorRelic implements OnSynergySubscriber
{
    public static final String ID = CreateFullID(LivingPicture.class);
    public int index;

    public LivingPicture()
    {
        this(0);
    }

    public LivingPicture(int index)
    {
        super(ID, RelicTier.STARTER, LandingSound.MAGICAL);

        if ((this.index = index) != 0)
        {
            RefreshTexture();
        }
    }

    public void ApplyEnchantment(Enchantment enchantment)
    {
        this.index = enchantment.index;
        RefreshTexture();
    }

    public CardGroup CreateUpgradeGroup()
    {
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        group.group.add(new Enchantment1());
        group.group.add(new Enchantment2());
        group.group.add(new Enchantment3());
        return group;
    }

    public void RefreshTexture()
    {
        if (index == 0)
        {
            setTexture(GR.GetTexture(GR.GetRelicImage(relicId)));
        }
        else
        {
            setTexture(GR.GetTexture(GR.GetRelicImage(relicId + "_" + index)));
        }
    }

    @Override
    public void onVictory()
    {
        super.onVictory();

        SetEnabled(true);
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        CombatStats.onSynergy.SubscribeOnce(this);
        SetEnabled(true);
    }

    @Override
    public void OnSynergy(AbstractCard c)
    {
        GameActions.Bottom.Draw(1);
        SetEnabled(false);
        flash();
    }
}