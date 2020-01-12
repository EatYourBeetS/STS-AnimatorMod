package eatyourbeets.cards.animator.series.Konosuba;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.subscribers.OnCostRefreshSubscriber;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class YunYun extends AnimatorCard implements Spellcaster, OnCostRefreshSubscriber, StartupCard
{
    public static final String ID = Register(YunYun.class, EYBCardBadge.Special);

    private int costModifier = 0;

    public YunYun()
    {
        super(ID, 0, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);

        Initialize(8, 0);
        SetUpgrade(4, 0);

        SetMultiDamage(true);
        SetSynergy(Synergies.Konosuba);
    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp)
    {
        return super.calculateModifiedCardDamage(player, mo, tmp + Spellcaster.GetScaling());
    }

    @Override
    public void resetAttributes()
    {
        super.resetAttributes();

        costModifier = 0;
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        costModifier = 0;
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        super.triggerOnEndOfTurnForPlayingCard();

        costModifier = 0;
    }

    @Override
    public AbstractCard makeStatEquivalentCopy()
    {
        YunYun copy = (YunYun) super.makeStatEquivalentCopy();

        copy.costModifier = this.costModifier;

        return copy;
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        OnCostRefresh(this);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.SFX("ORB_LIGHTNING_EVOKE");

        for (AbstractMonster m1 : GameUtilities.GetCurrentEnemies(true))
        {
            GameActions.Bottom.VFX(new LightningEffect(m1.drawX, m1.drawY));
        }

        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.NONE);

        if (!EffectHistory.HasActivatedSemiLimited(cardID))
        {
            int lightning = 0;
            for (AbstractOrb orb : AbstractDungeon.player.orbs)
            {
                if (Lightning.ORB_ID.equals(orb.ID))
                {
                    lightning += 1;
                }
            }

            if (lightning >= 3)
            {
                GameActions.Bottom.GainIntellect(1);
                EffectHistory.TryActivateSemiLimited(cardID);
            }
        }
    }

    @Override
    public void OnCostRefresh(AbstractCard card)
    {
        if (card == this)
        {
            int attacks = 0;
            for (AbstractCard c : AbstractDungeon.player.hand.group)
            {
                if (c != this && c.type == CardType.ATTACK)
                {
                    attacks += 1;
                }
            }

            int currentCost = (costForTurn - costModifier);

            costModifier = attacks;

            if (!this.freeToPlayOnce)
            {
                this.setCostForTurn(currentCost + costModifier);
            }
        }
    }

    @Override
    public boolean atBattleStartPreDraw()
    {
        if (GameUtilities.IsEliteRoom())
        {
            GameActions.Bottom.ChannelOrb(new Lightning(), false);

            return true;
        }

        return false;
    }
}