package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.subscribers.OnEndOfTurnSubscriber;

public class Demiurge extends AnimatorCard implements OnEndOfTurnSubscriber
{
    public static final String ID = CreateFullID(Demiurge.class.getSimpleName());

    public Demiurge()
    {
        super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0,0,4, 6);

        SetSynergy(Synergies.Overlord);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (upgraded)
        {
            GameActionsHelper.GainEnergy(2);
        }
        else
        {
            GameActionsHelper.GainEnergy(1);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        if (upgraded)
        {
            GameActionsHelper.GainEnergy(2);
        }
        else
        {
            GameActionsHelper.GainEnergy(1);
        }

        PlayerStatistics.onEndOfTurn.Subscribe(this);
    }

    @Override
    public void upgrade() 
    {
        TryUpgrade();
    }

    @Override
    public void OnEndOfTurn(boolean isPlayer)
    {
        AbstractPlayer p = AbstractDungeon.player;

        AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(this.makeStatEquivalentCopy()));
        if (upgraded)
        {
            GameActionsHelper.DamageTarget(p, p, this.secondaryValue, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        }
        else
        {
            GameActionsHelper.DamageTarget(p, p, this.magicNumber, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        }
        PlayerStatistics.onEndOfTurn.Unsubscribe(this);
    }
}