package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import com.megacrit.cardcrawl.vfx.combat.PowerIconShowEffect;
import eatyourbeets.cards.animator.Nanami;
import eatyourbeets.utilities.RandomizedList;

public class KaijinPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(KaijinPower.class.getSimpleName());

    public KaijinPower(AbstractPlayer owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;

        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        AbstractDungeon.effectsQueue.add(new PowerIconShowEffect(this));

        boolean used = false;
        AbstractPlayer p = AbstractDungeon.player;
        RandomizedList<AbstractCard> cards = new RandomizedList<>(p.drawPile.group);
        while (cards.Count() > 0)
        {
            AbstractCard card = cards.Retrieve(AbstractDungeon.miscRng);
            if (!card.cardID.equals(Nanami.ID))
            {
                if (card.baseDamage > 0)
                {
                    card.baseDamage += amount;
                    used = true;
                }
                if (card.baseBlock > 0)
                {
                    card.baseBlock += amount;
                    used = true;
                }
            }

            if (used)
            {
                AbstractDungeon.effectsQueue.add(new UpgradeShineEffect((float) Settings.WIDTH / 4.0F, (float) Settings.HEIGHT / 2.0F));
                AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(card.makeStatEquivalentCopy(), (float) Settings.WIDTH / 4.0F, (float) Settings.HEIGHT / 2.0F));
                return;
            }
        }
    }
}
