package eatyourbeets.cards.animator.beta.DateALive;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class InverseTohka extends AnimatorCard {
    public static final EYBCardData DATA = Register(InverseTohka.class).SetAttack(1, CardRarity.SPECIAL, EYBAttackType.Normal, EYBCardTarget.ALL);

    public InverseTohka() {
        super(DATA);

        Initialize(10, 0, 10);

        SetSynergy(Synergies.DateALive);
    }

    @Override
    protected void OnUpgrade() {
        SetRetain(true);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        int energy = EnergyPanel.getCurrentEnergy();
        if (energy > 0)
        {
            GameActions.Bottom.GainEnergy(-1);

            int[] damageMatrix = DamageInfo.createDamageMatrix(magicNumber, true);

            GameActions.Bottom.DealDamageToAll(damageMatrix, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.SLASH_HEAVY);
            GameActions.Bottom.Add(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED));
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        GameActions.Bottom.Reload(name, cards ->
        {
            if (cards.size() > 0) {

                for (int i=0; i<cards.size(); i++)
                {
                    GameActions.Top.DealDamageToAll(this, AbstractGameAction.AttackEffect.SLASH_DIAGONAL)
                            .SetOptions(true,false);
                }
            }
        });
        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        GameActions.Bottom.Add(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.LONG, ScreenShake.ShakeIntensity.HIGH));
    }
}