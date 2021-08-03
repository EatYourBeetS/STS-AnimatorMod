package eatyourbeets.cards.animator.ultrarare;

import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.cards.colorless.Madness;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.effects.VFX;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Cthulhu extends AnimatorCard_UltraRare
{
    public static final int COST = 12;
    public static final EYBCardData DATA = Register(Cthulhu.class)
            .SetAttack(-1, CardRarity.SPECIAL, EYBAttackType.Elemental, EYBCardTarget.ALL)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.CallOfCthulhu);

    public Cthulhu()
    {
        super(DATA);

        Initialize(800, 0, 120);

        SetAffinity_Dark(2, 0, 32);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    protected ColoredString GetCostString()
    {
        ColoredString res = super.GetCostString();
        res.text = String.valueOf(COST);
        return res;
    }

    @Override
    public boolean hasEnoughEnergy()
    {
        this.costForTurn = COST;
        this.freeToPlayOnce = false;
        boolean res = super.hasEnoughEnergy();
        this.costForTurn = -2;
        return res;
    }

    @Override
    public boolean freeToPlay()
    {
        return false;
    }

    @Override
    public boolean canUpgrade()
    {
        return false;
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.MakeCardInHand(new Madness());
        GameActions.Bottom.Flash(this);
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        GameUtilities.ModifyCostForCombat(this, COST, false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (EnergyPanel.getCurrentEnergy() < COST)
        {
            return;
        }

        EnergyPanel.useEnergy(COST);
        GameActions.Bottom.GainTemporaryHP(magicNumber);
        GameActions.Bottom.VFX(VFX.Cataclysm(), 0.8f, true)
        .AddCallback(__ -> GameActions.Top.DealDamageToAll(this, AttackEffects.NONE));
    }
}