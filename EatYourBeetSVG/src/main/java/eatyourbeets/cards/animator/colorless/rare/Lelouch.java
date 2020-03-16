package eatyourbeets.cards.animator.colorless.rare;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.GeassPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Lelouch extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Lelouch.class).SetSkill(3, CardRarity.RARE, EYBCardTarget.ALL).SetColor(CardColor.COLORLESS);

    public Lelouch()
    {
        super(DATA);

        Initialize(0, 0, 3);
        SetCostUpgrade(-1);

        SetExhaust(true);
        SetSynergy(Synergies.CodeGeass);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Top.ExhaustFromHand(name, magicNumber, true).ShowEffect(true, true)
        .SetOptions(true, true, true);

        GameActions.Bottom.VFX(new BorderFlashEffect(Color.RED));
        GameActions.Bottom.SFX("MONSTER_COLLECTOR_DEBUFF");

        for (AbstractMonster enemy : GameUtilities.GetAllEnemies(true))
        {
            if (!enemy.hasPower(GeassPower.POWER_ID))
            {
                GameActions.Bottom.ApplyPower(p, enemy, new GeassPower(enemy));
            }
        }
    }
}