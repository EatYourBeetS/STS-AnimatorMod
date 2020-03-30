package eatyourbeets.cards.animator.beta.MadokaMagica;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.CollectorCurseEffect;
import com.megacrit.cardcrawl.vfx.combat.MindblastEffect;
import eatyourbeets.actions.animator.CreateRandomCurses;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.utilities.GameActions;

public class Oktavia extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Oktavia.class).SetAttack(2, CardRarity.SPECIAL, EYBAttackType.Elemental, EYBCardTarget.ALL);

    public Oktavia()
    {
        super(DATA);

        Initialize(9, 0, 1, 2);
        SetUpgrade(2, 0, 0, 0);
        SetScaling(1, 0, 0);

        SetSynergy(Synergies.MadokaMagica);
        SetSpellcaster();
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        magicNumber = player.hand.getCardsOfType(CardType.CURSE).size();
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.Callback(new CreateRandomCurses(secondaryValue, p.hand), m, (m1, __) ->
        {
            GameActions.Bottom.Draw(player.hand.getCardsOfType(CardType.CURSE).size())
            .AddCallback(m1, (enemy, cards) ->
            {
                for (int i = 0; i < player.hand.getCardsOfType(CardType.CURSE).size(); i++)
                {
                    GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.NONE)
                            .SetVFX(true, false);
                }

                GameActions.Bottom.VFX(new BorderFlashEffect(Color.BLACK));
                GameActions.Bottom.SFX("MONSTER_COLLECTOR_DEBUFF");
                GameActions.Bottom.VFX(new MindblastEffect(p.dialogX, p.dialogY, p.flipHorizontal), 0.2f);
                GameActions.Bottom.Add(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.HIGH));
            });
        });
    }
}
