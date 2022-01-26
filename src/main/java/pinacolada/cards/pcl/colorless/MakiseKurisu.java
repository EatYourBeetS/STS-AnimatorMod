package pinacolada.cards.pcl.colorless;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.actions.watcher.SkipEnemiesTurnAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.TimeWarpTurnEndEffect;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.special.TimeParadox;
import pinacolada.utilities.PCLActions;

public class MakiseKurisu extends PCLCard
{
    public static final PCLCardData DATA = Register(MakiseKurisu.class)
            .SetSkill(3, CardRarity.RARE, PCLCardTarget.None)
            .SetMaxCopies(1)
            .SetColor(CardColor.COLORLESS).SetSeries(CardSeries.SteinsGate)
            .PostInitialize(data ->
                data.AddPreview(new TimeParadox(), false)
            );

    public MakiseKurisu()
    {
        super(DATA);

        Initialize(0, 0, 1, 2);

        SetAffinity_Blue(1);
        SetAffinity_Silver(1);

        SetExhaust(true);
        SetEthereal(true);
    }

    @Override
    protected void OnUpgrade()
    {
        super.OnUpgrade();
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.SFX("POWER_TIME_WARP", 0.9F, 1.1f);
        PCLActions.Bottom.VFX(new TimeWarpTurnEndEffect());
        PCLActions.Bottom.VFX(new BorderFlashEffect(Color.VIOLET, true));
        PCLActions.Bottom.Add(new SkipEnemiesTurnAction());
        PCLActions.Bottom.DrawNextTurn(magicNumber);
        PCLActions.Bottom.GainEnergyNextTurn(magicNumber);
        for (int i = 0; i < secondaryValue; i++) {
            PCLActions.Bottom.MakeCardInDiscardPile(new TimeParadox());
        }
        PCLActions.Bottom.Add(new PressEndTurnButtonAction());
    }
}
