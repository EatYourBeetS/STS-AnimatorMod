package eatyourbeets.cards.unnamed.uncommon;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class Obfuscate extends UnnamedCard
{
    public static final EYBCardData DATA = Register(Obfuscate.class)
            .SetMaxCopies(2)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);

    public Obfuscate()
    {
        super(DATA);

        Initialize(0, 0, 1, 5);
        SetUpgrade(0, 0, 0, 2);

        SetRecast(true);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        for (EnemyIntent intent : GameUtilities.GetIntents())
        {
            intent.AddWithering(secondaryValue);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.BorderFlash(Color.PURPLE);
        GameActions.Bottom.GainTemporaryStats(0, magicNumber, 0);
        GameActions.Bottom.StackWithering(TargetHelper.Enemies(p), secondaryValue);
    }
}