package eatyourbeets.cards.animator.colorless.rare;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.powers.animator.GeassPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class Lelouch extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Lelouch.class).SetSkill(3, CardRarity.RARE, EYBCardTarget.ALL).SetColor(CardColor.COLORLESS);
    public static final PowerHelper GEASS = new PowerHelper(GeassPower.POWER_ID, null, (o, s, a) -> new GeassPower(o));

    public Lelouch()
    {
        super(DATA);

        Initialize(0, 0, 3);
        SetCostUpgrade(-1);

        SetExhaust(true);
        SetSeries(CardSeries.CodeGeass);

        SetAffinity_Blue(2);
        SetAffinity_Dark(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Top.ExhaustFromHand(name, magicNumber, true).ShowEffect(true, true)
        .SetOptions(true, true, true);

        GameActions.Bottom.VFX(new BorderFlashEffect(Color.RED));
        GameActions.Bottom.SFX("MONSTER_COLLECTOR_DEBUFF");
        GameActions.Bottom.ApplyPower(TargetHelper.Enemies(), GEASS);
    }
}