package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.VerticalImpactEffect;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

import java.util.ArrayList;

public class Saitama extends AnimatorCard
{
    public static final String ID = CreateFullID(Saitama.class.getSimpleName());

    public Saitama()
    {
        super(ID, 2, CardType.ATTACK, CardColor.COLORLESS, CardRarity.RARE, CardTarget.ENEMY);

        Initialize(50, 0);

        this.isInnate = true;
        this.retain = true;

        SetSynergy(Synergies.OnePunchMan);
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        this.retain = true;
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        super.triggerOnEndOfTurnForPlayingCard();

        this.retain = true;
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        boolean playable = super.cardPlayable(m);
        if (playable)
        {
            ArrayList<AbstractCard> cards = AbstractDungeon.player.hand.group;
            for (AbstractCard c : cards)
            {
                if ((c instanceof Saitama) && c.costForTurn < 2)
                {
                    return false;
                }
            }
        }

        return playable;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.AddToBottom(new VFXAction(new VerticalImpactEffect(m.hb.cX + m.hb.width / 4.0F, m.hb.cY - m.hb.height / 4.0F)));

        if (upgraded)
        {
            GameActionsHelper.DamageTargetPiercing(p, m, this, AbstractGameAction.AttackEffect.NONE);
        }
        else
        {
            GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.NONE);
        }

        GameActionsHelper.AddToBottom(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED));
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(10);
        }
    }
}