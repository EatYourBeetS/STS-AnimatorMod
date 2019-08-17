package eatyourbeets.cards.animator;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import eatyourbeets.powers.animator.DemiurgePower;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.interfaces.OnEndOfTurnSubscriber;

public class Demiurge extends AnimatorCard
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

        GameActionsHelper.GainEnergy(GetEnergyGain());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        int energy = GetEnergyGain();
        int stacks = GetSelfDamage();

        GameActionsHelper.GainEnergy(energy);
        GameActionsHelper.ApplyPowerSilently(p, p, new DemiurgePower(p, stacks), stacks);
    }

    @Override
    public void upgrade() 
    {
        TryUpgrade();
    }

    private int GetEnergyGain()
    {
        return upgraded ? 2 : 1;
    }

    private int GetSelfDamage()
    {
        return upgraded ? secondaryValue : magicNumber;
    }
}