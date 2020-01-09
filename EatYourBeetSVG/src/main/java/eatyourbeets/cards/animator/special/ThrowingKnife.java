package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.vfx.combat.ThrowDaggerEffect;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorResources;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

public abstract class ThrowingKnife extends AnimatorCard implements Hidden
{
    public static final String ID = Register(ThrowingKnife.class);

    private static RandomizedList<ThrowingKnife> subTypes = null;

    public ThrowingKnife(String id)
    {
        super(staticCardData.get(id), id, AnimatorResources.GetCardImage(ID), 0, CardType.ATTACK, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.ENEMY);

        this.tags.add(GR.Enums.CardTags.PURGE);
    }

    public static AbstractCard GetRandomCard()
    {
        if (subTypes == null)
        {
            subTypes = new RandomizedList<>();
            subTypes.Add(new ThrowingKnife_0());
            subTypes.Add(new ThrowingKnife_1());
            subTypes.Add(new ThrowingKnife_2());
        }

        if (AbstractDungeon.cardRandomRng != null)
        {
            return subTypes.Retrieve(AbstractDungeon.cardRandomRng, false).makeCopy();
        }
        else
        {
            return subTypes.Retrieve(new Random(), false).makeCopy();
        }
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Bottom.Callback(__ ->
        {
            AbstractDungeon.player.discardPile.removeCard(this);
            this.freeToPlayOnce = true;
            this.purgeOnUse = true;
            this.applyPowers();
            this.use(AbstractDungeon.player, null);
        });
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (m == null || m.isDeadOrEscaped())
        {
            m = GameUtilities.GetRandomEnemy(true);
        }

        if (m != null)
        {
            AddSecondaryEffect(p, m);

            GameActions.Top.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE)
                    .SetOptions(true, false);

            if (m.hb != null)
            {
                GameActions.Top.VFX(new ThrowDaggerEffect(m.hb.cX, m.hb.cY));
            }
        }
    }

    protected abstract void AddSecondaryEffect(AbstractPlayer p, AbstractMonster m);
}